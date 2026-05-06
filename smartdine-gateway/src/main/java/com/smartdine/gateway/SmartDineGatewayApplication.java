package com.smartdine.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 智膳云 API 网关服务
 * 基于 Spring Cloud Gateway + Nacos 实现动态路由、负载均衡、限流熔断
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SmartDineGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartDineGatewayApplication.class, args);
    }

}
