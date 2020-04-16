package ru.kpfu.itis.meow.util;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MqttSender {

    private IMqttClient publisher;

    public MqttSender() {
        try {
            String publisherId = UUID.randomUUID().toString();
            publisher = new MqttClient("tcp://broker.mqttdashboard.com:1883", publisherId);

            connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void sendMqttMessage(boolean m, String deviceName) {
        String message = m ? "true" : "false";
        sendMqttMessage(message, deviceName);
    }

    private void connect() throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        publisher.connect(options);
    }

    private void sendMqttMessage(String message, String deviceName) {
        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(2);
            publisher.publish("paho/meow/" + deviceName + "/notification", mqttMessage);

        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
}
