package com.smartdine;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.smartdine.mapper")
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableFeignClients
@Slf4j
@EnableCaching
public class SmartDineApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartDineApplication.class, args);
        log.info("智膳云平台服务启动成功");
    }
}
