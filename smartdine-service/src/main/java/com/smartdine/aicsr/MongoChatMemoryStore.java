package com.smartdine.aicsr;

import com.smartdine.aicsr.bean.ChatMessages;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * MongoDB 聊天记忆存储实现类
 * 用于持久化存储AI客服的对话历史记录
 */
@Slf4j
@Component
public class MongoChatMemoryStore implements ChatMemoryStore {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 根据 memoryId 获取聊天记录列表
     *
     * @param memoryId 对话唯一标识
     * @return 聊天记录列表，如果没有记录则返回空列表
     */
    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        try {
            // 使用 id 字段查询（对应 MongoDB 的 _id）
            Criteria criteria = Criteria.where("id").is(memoryId);
            Query query = new Query(criteria);

            ChatMessages chatMessages = mongoTemplate.findOne(query, ChatMessages.class);
            if (chatMessages == null || chatMessages.getContent() == null) {
                return new LinkedList<>();
            }

            String content = chatMessages.getContent();
            return ChatMessageDeserializer.messagesFromJson(content);
        } catch (Exception e) {
            log.error("获取聊天记录失败, memoryId: {}", memoryId, e);
            return new LinkedList<>();
        }
    }

    /**
     * 更新或插入聊天记录
     * 使用 upsert 操作，如果记录不存在则自动创建
     *
     * @param memoryId 对话唯一标识
     * @param messages 聊天记录列表
     */
    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        try {
            Criteria criteria = Criteria.where("id").is(memoryId);
            Query query = new Query(criteria);
            Update update = new Update();

            // 将消息列表序列化为 JSON 字符串
            String content = ChatMessageSerializer.messagesToJson(messages);
            update.set("content", content);
            update.set("updateTime", LocalDateTime.now());

            // 如果是新记录，设置创建时间
            update.setOnInsert("createTime", LocalDateTime.now());
            update.setOnInsert("id", memoryId);

            // upsert: 如果没有对应 id 则直接插入，否则更新
            mongoTemplate.upsert(query, update, ChatMessages.class);
            log.debug("更新聊天记录成功, memoryId: {}, 消息数量: {}", memoryId, messages.size());
        } catch (Exception e) {
            log.error("更新聊天记录失败, memoryId: {}", memoryId, e);
        }
    }

    /**
     * 删除指定对话的聊天记录
     *
     * @param memoryId 对话唯一标识
     */
    @Override
    public void deleteMessages(Object memoryId) {
        try {
            Criteria criteria = Criteria.where("id").is(memoryId);
            Query query = new Query(criteria);
            mongoTemplate.remove(query, ChatMessages.class);
            log.info("删除聊天记录成功, memoryId: {}", memoryId);
        } catch (Exception e) {
            log.error("删除聊天记录失败, memoryId: {}", memoryId, e);
        }
    }

    /**
     * 获取所有历史会话列表（按更新时间倒序）
     * 用于展示历史会话列表
     *
     * @return 历史会话列表
     */
    public List<ChatMessages> getAllSessions() {
        try {
            Query query = new Query();
            query.with(org.springframework.data.domain.Sort.by(
                    org.springframework.data.domain.Sort.Direction.DESC, "updateTime"));
            return mongoTemplate.find(query, ChatMessages.class);
        } catch (Exception e) {
            log.error("获取历史会话列表失败", e);
            return new LinkedList<>();
        }
    }

    /**
     * 更新会话标题
     *
     * @param memoryId 对话唯一标识
     * @param title    会话标题
     */
    public void updateSessionTitle(Object memoryId, String title) {
        try {
            Criteria criteria = Criteria.where("id").is(memoryId);
            Query query = new Query(criteria);
            Update update = new Update();
            update.set("title", title);
            update.set("updateTime", LocalDateTime.now());
            mongoTemplate.updateFirst(query, update, ChatMessages.class);
        } catch (Exception e) {
            log.error("更新会话标题失败, memoryId: {}", memoryId, e);
        }
    }
}
