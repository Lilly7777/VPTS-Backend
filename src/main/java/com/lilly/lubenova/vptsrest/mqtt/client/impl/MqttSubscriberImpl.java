package com.lilly.lubenova.vptsrest.mqtt.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilly.lubenova.vptsrest.model.GPSRecord;
import com.lilly.lubenova.vptsrest.repository.GPSRepository;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Component("MqttSubscriber")
public class MqttSubscriberImpl implements MqttCallback {

    // Values are injected by the Environment variables
    private final String broker;
    private final int qos;
    private final Boolean hasSSL;
    private final Integer port;
    private final String userName;
    private final String password;

    // Changeable from others
    private String brokerUrl;
    private final String clientId;

    // MQTT specific
    private MqttClient mqttClient;
    private MqttConnectOptions connectionOptions;
    private MemoryPersistence persistence;

    // DB Reactive repository
    private final GPSRepository gpsRepository;

    private static final Logger logger = LoggerFactory.getLogger(MqttSubscriberImpl.class);

    public MqttSubscriberImpl(Environment environment, GPSRepository gpsRepository) {
        this.gpsRepository = gpsRepository;
        this.broker = Objects.requireNonNull(environment.getProperty("vpts.mqtt.broker.ip"));
        this.qos = Integer.parseInt(Objects.requireNonNull(environment.getProperty("vpts.mqtt.qos")));
        this.hasSSL = Objects.requireNonNull(
                environment.getProperty("vpts.mqtt.broker.ssl.enabled")).equals("true");
        this.port = Integer.parseInt(Objects.requireNonNull(environment.getProperty("vpts.mqtt.broker.port")));
        this.userName = Objects.requireNonNull(environment.getProperty("vpts.mqtt.client.username"));
        this.password = Objects.requireNonNull(environment.getProperty("vpts.mqtt.client.password"));

        this.clientId = UUID.randomUUID().toString();
        this.connectionOptions = null;
        this.persistence = null;
        this.brokerUrl = null;

        this.config();
    }

    @PreDestroy
    @DependsOn("MqttMessageListener")
    public void destroy() {
        this.disconnect();
    }

    @Override
    public void connectionLost(Throwable cause) {
        logger.info("Connection lost", cause);
        this.config();
    }

    protected void config() {
        this.brokerUrl = String.format("%s://%s:%s", "tcp", this.broker, this.port);
        this.persistence = new MemoryPersistence();
        this.connectionOptions = new MqttConnectOptions();
        try {
            this.mqttClient = new MqttClient(brokerUrl, clientId, persistence);
            this.connectionOptions.setCleanSession(true);
            this.connectionOptions.setPassword(this.password.toCharArray());
            this.connectionOptions.setUserName(this.userName);
            this.mqttClient.connect(this.connectionOptions);
            this.mqttClient.setCallback(this);
        } catch (MqttException me) {
            logger.error(me.getMessage(), me);
            throw new IllegalStateException("Not connected");
        }
    }

    public void subscribeMessage(String topic) {
        try {
            this.mqttClient.subscribe(topic, this.qos);
        } catch (MqttException me) {
            logger.warn("Not able to read topic - {}", topic);
            logger.error(me.getMessage(), me);
        }
    }

    public void disconnect() {
        try {
            this.mqttClient.disconnect();
            logger.info("Disconnected from broker: {}", brokerUrl);
        } catch (MqttException me) {
            logger.error(me.getMessage(), me);
        }
    }

    @Override
    public void messageArrived(String mqttTopic, MqttMessage mqttMessage) throws Exception {
        Date time = new Date();
        logger.debug("=======================================================================");
        logger.debug("Message arrived at time {} on topic {}", time, mqttTopic);
        logger.debug("Message: {}", mqttMessage.getPayload());
        logger.debug("=======================================================================\n");
        try {
            GPSRecord gpsRecord = new ObjectMapper().readValue(mqttMessage.getPayload(), GPSRecord.class);
            if (StringUtils.substringAfter(mqttTopic, "device/").equals(gpsRecord.getDeviceId())) {
                gpsRepository.save(gpsRecord).block();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {}
}
