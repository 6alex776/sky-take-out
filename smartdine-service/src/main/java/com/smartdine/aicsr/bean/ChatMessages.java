package com.smartdine.aicsr.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * MongoDB 聊天记录实体类
 * 用于存储AI客服的对话历史记录
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("chat_messages")
public class ChatMessages {

    /**
     * 唯一标识，使用 memoryId 作为 MongoDB 文档的 _id 字段
     * 这样可以方便地通过 memoryId 查询和更新记录
     */
    @Id
    private Long id;

    /**
     * 聊天记录内容，存储为 JSON 字符串
     * 包含用户和AI的对话消息列表
     */
    private String content;

    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;

    /**
     * 记录最后更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 对话标题（可选，用于展示历史会话列表）
     */
    private String title;

    /**
     * 用户ID（可选，用于关联具体用户）
     */
    private Long userId;
}
