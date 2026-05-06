package com.smartdine.ai.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 智膳云 AI Agent 服务
 * 六大智能Agent的核心引擎：经营顾问、调度指挥、用户服务、营销创意、供应链、数据洞察
 */
@SpringBootApplication(scanBasePackages = {"com.smartdine.ai.agent", "com.smartdine"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.smartdine.ai.agent")
public class SmartDineAiAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartDineAiAgentApplication.class, args);
    }

}
