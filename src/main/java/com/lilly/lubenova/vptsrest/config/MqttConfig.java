package com.lilly.lubenova.vptsrest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MqttConfig {

    @Value("${vpts.mqtt.broker.ip}")
    public String broker;

    @Value("${vpts.mqtt.qos}")
    public int qos;

    @Value("${vpts.mqtt.broker.ssl.enabled}")
    public Boolean hasSSL;

    @Value("${vpts.mqtt.broker.port}")
    public Integer port;

    @Value("${vpts.mqtt.client.username}")
    public String userName;

    @Value("${vpts.mqtt.client.password}")
    public String password;

    public final String TCP = "tcp://";

    public final String SSL = "ssl://";

}
