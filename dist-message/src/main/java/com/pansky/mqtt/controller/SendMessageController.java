package com.pansky.mqtt.controller;

import com.alibaba.nacos.common.utils.StringUtils;
import com.pansky.mqtt.config.MqttOutputApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Fo
 * @date 2023/5/20 10:02
 */
@RestController
@RequestMapping("/msg")
public class SendMessageController {
    @Autowired
    private MqttOutputApi mqttOutputApi;

    /**
     * 发送mqtt消息
     * @param topic 消息主题
     * @param data  消息内容
     * @return
     */
    @GetMapping("/send1")
    public String sendMsg(String topic, String data){

        if (StringUtils.isNotBlank(topic)) {
            this.mqttOutputApi.sendToMqtt(topic, data);
        } else {
            this.mqttOutputApi.sendToMqtt(data);
        }
        return "success";
    }
}
