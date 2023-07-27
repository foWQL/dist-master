package com.pansky.mqtt.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

/**
 * @author Fo
 * @date 2023/5/19 17:50
 * @Describe mqtt 接受服务端消息
 */
@Configuration
@Slf4j
public class MqttInConfig {

    /**
     * mqtt消息入站通道，订阅消息后消息进入的通道。
     * 可创建多个入站通道，对应多个不同的消息生产者。
     *
     * @return
     */
    @Bean
    public MessageChannel myInputChannel() {
        return new DirectChannel();
    }
    /**
     * 对于当前应用来讲，接收的mqtt消息的生产者。将生产者绑定到mqtt入站通道，即通过入站通道进入的消息为生产者生产的消息。
     * 可创建多个消息生产者，对应多个不同的消息入站通道，同时生产者监听不同的topic
     *
     * @return
     */
    @Bean
    public MessageProducer channelInbound(MessageChannel myInputChannel, MqttPahoClientFactory factory) {
        String clientId = "h-backend-mqtt-in-" + System.currentTimeMillis();
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(clientId, factory, "/crane/mqtt/demo/#");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(myInputChannel);
        return adapter;
    }
    /**
     * mqtt入站消息处理工具，对于指定消息入站通道接收到生产者生产的消息后处理消息的工具。
     *
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler mqttMessageHandler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String topic = String.valueOf(message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC));
                String payload = String.valueOf(message.getPayload());
                System.out.println(message.getPayload());
                log.info("接收到 mqtt消息，主题:{} 消息:{}", topic, payload);
            }
        };
    }

}
