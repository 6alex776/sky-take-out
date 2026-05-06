package com.smartdine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket配置类
 * 用于注册WebSocket端点，启用WebSocket功能
 */
@Configuration
public class WebSocketConfiguration {

    /**
     * 注册ServerEndpointExporter Bean
     * 该Bean会自动扫描并注册使用了@ServerEndpoint注解的WebSocket端点
     *
     * @return ServerEndpointExporter实例
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
