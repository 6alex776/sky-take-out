package com.smartdine.ai.agent.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MongoDB配置类
 */
@Configuration
public class MongoConfig {

    @Value("${smartdine.mongodb.uri:mongodb://localhost:27017/smartdine}")
    private String mongoUri;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUri);
    }

    @Bean
    public MongoDatabase mongoDatabase(MongoClient mongoClient) {
        // 从URI解析数据库名，或使用默认的smartdine
        String dbName = "smartdine";
        if (mongoUri.contains("/")) {
            String[] parts = mongoUri.split("/");
            String lastPart = parts[parts.length - 1];
            if (lastPart.contains("?")) {
                dbName = lastPart.substring(0, lastPart.indexOf("?"));
            } else if (!lastPart.isEmpty()) {
                dbName = lastPart;
            }
        }
        return mongoClient.getDatabase(dbName);
    }
}
