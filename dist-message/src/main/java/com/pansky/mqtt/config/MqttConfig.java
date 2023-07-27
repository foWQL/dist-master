package com.pansky.mqtt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Fo
 * @date 2023/5/19 17:52
 */

@Component
@ConfigurationProperties(prefix = "mqtt")
@Data
public class MqttConfig {

    private String host;
    private String username;
    private String password;
    private String defaultTopic;

}
