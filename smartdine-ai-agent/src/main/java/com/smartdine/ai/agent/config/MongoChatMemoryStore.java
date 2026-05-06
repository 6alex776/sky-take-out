package com.smartdine.ai.agent.config;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static dev.langchain4j.data.message.ChatMessageDeserializer.messagesFromJson;
import static dev.langchain4j.data.message.ChatMessageSerializer.messagesToJson;

/**
 * MongoDB聊天记忆存储
 * 将AI对话历史存储到MongoDB，支持分布式部署
 */
@Component
public class MongoChatMemoryStore implements ChatMemoryStore {

    private final MongoCollection<Document> collection;

    @Autowired
    public MongoChatMemoryStore(MongoDatabase mongoDatabase) {
        this.collection = mongoDatabase.getCollection("agent_chat_memory");
    }

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        Document doc = collection.find(Filters.eq("memoryId", memoryId.toString())).first();
        if (doc == null) {
            return List.of();
        }
        String json = doc.getString("messages");
        return messagesFromJson(json);
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        String json = messagesToJson(messages);
        Document doc = new Document();
        doc.put("memoryId", memoryId.toString());
        doc.put("messages", json);
        doc.put("updateTime", System.currentTimeMillis());

        collection.replaceOne(
                Filters.eq("memoryId", memoryId.toString()),
                doc,
                new ReplaceOptions().upsert(true)
        );
    }

    @Override
    public void deleteMessages(Object memoryId) {
        collection.deleteOne(Filters.eq("memoryId", memoryId.toString()));
    }
}
