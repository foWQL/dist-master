package com.pansky.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * @author Fo
 * @date 2023/5/19 17:50
 * @Describe  mqtt向服务端发送 消息
 */
@Configuration
public class MqttOutConfig {

    @Bean
    public MessageChannel mqttOutChannel() {
        return new DirectChannel();
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory(){
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
//        factory.setServerURIs(mqttConfig.getServers());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[] { "tcp://localhost:1883" });
        options.setUserName("guest");
        options.setPassword("guest".toCharArray());
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutChannel")
    public MessageHandler mqttOutbound(MqttPahoClientFactory factory) {
        String clientId = "h-backend-mqtt-out-" + System.currentTimeMillis();
        MqttPahoMessageHandler messageHandler =
                new MqttPahoMessageHandler(clientId, factory);
        messageHandler.setAsync(true);
//        messageHandler.setDefaultQos(2);
        messageHandler.setDefaultTopic("/crane/mqtt/demo/foo");
        return messageHandler;
    }


}
