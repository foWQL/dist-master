package com.pansky.mqtt.config;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * @author Fo
 * @date 2023/5/20 9:59
 */
@Component
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttOutputApi {

    // 向默认的 topic 发送消息
    void sendToMqtt(String payload);

    // 向指定的 topic 发送消息
    void sendToMqtt(String payload,@Header(MqttHeaders.TOPIC) String topic);

    // 向指定的 topic 发送消息，并指定服务质量参数
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, String payload);

}
