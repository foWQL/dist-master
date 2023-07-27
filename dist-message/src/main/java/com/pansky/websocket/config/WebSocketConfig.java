package com.pansky.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author Fo
 * @date 2023/5/15 17:17
 */
@Configuration
public class WebSocketConfig {

    @Bean("serverEndpointExporter")
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

}
