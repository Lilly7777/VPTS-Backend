package com.lilly.lubenova.vptsrest.mqtt.client.listener;

import com.lilly.lubenova.vptsrest.mqtt.client.impl.MqttSubscriberImpl;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.atomic.AtomicBoolean;

@Component("MqttMessageListener")
public class MqttMessageListener implements Runnable {

    private final MqttSubscriberImpl subscriber;

    public MqttMessageListener(MqttSubscriberImpl subscriber) {
        this.subscriber = subscriber;
    }

    private final AtomicBoolean run = new AtomicBoolean(true);

    @PreDestroy
    public void stop() {
        run.set(false);
    }

    @Override
    public void run() {
        while (run.get()) {
            subscriber.subscribeMessage("device/+");
        }
    }
}


