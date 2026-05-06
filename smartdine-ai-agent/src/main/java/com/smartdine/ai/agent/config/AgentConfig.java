package com.smartdine.ai.agent.config;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * AI Agent配置类
 * 配置大语言模型和记忆存储
 */
@Configuration
public class AgentConfig {

    @Value("${smartdine.ai.api-key:}")
    private String apiKey;

    @Value("${smartdine.ai.base-url:https://api.openai.com/v1}")
    private String baseUrl;

    @Value("${smartdine.ai.model:gpt-4o-mini}")
    private String model;

    @Value("${smartdine.ai.timeout:60}")
    private int timeout;

    /**
     * 配置同步聊天模型
     */
    @Bean
    public dev.langchain4j.model.chat.ChatLanguageModel chatLanguageModel() {
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(model)
                .timeout(Duration.ofSeconds(timeout))
                .build();
    }

    /**
     * 配置流式聊天模型
     */
    @Bean
    public dev.langchain4j.model.chat.StreamingChatLanguageModel streamingChatLanguageModel() {
        return OpenAiStreamingChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(model)
                .timeout(Duration.ofSeconds(timeout))
                .build();
    }

    /**
     * 配置聊天记忆窗口
     */
    @Bean
    public MessageWindowChatMemory chatMemory(ChatMemoryStore chatMemoryStore) {
        return MessageWindowChatMemory.builder()
                .maxMessages(20)
                .chatMemoryStore(chatMemoryStore)
                .build();
    }
}
