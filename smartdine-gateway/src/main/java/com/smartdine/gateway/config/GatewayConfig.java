package com.smartdine.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

/**
 * 网关全局配置
 * 定义全局过滤器、跨域配置等
 */
@Slf4j
@Configuration
public class GatewayConfig {

    /**
     * 全局日志过滤器
     * 记录所有经过网关的请求信息
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter loggingFilter() {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            String method = exchange.getRequest().getMethod().name();
            log.info("[Gateway] {} {}", method, path);

            long startTime = System.currentTimeMillis();
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[Gateway] {} {} completed in {}ms", method, path, duration);
            }));
        };
    }

}
