package com.smartdine.aicsr.Impl;

import com.smartdine.aicsr.ChatService;
import com.smartdine.aicsr.MongoChatMemoryStore;
import com.smartdine.aicsr.assistant.CsrAgent;
import com.smartdine.aicsr.bean.ChatMessages;
import com.smartdine.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * AI智能客服服务实现类
 * 实现对话、历史记录管理等功能
 */
@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private CsrAgent csrAgent;

    @Autowired
    private MongoChatMemoryStore mongoChatMemoryStore;

    /**
     * 与AI进行对话（流式输出）
     *
     * @param memoryId 对话唯一标识，用于保持上下文
     * @param message  用户输入的消息
     * @return 流式返回AI的回复
     */
    @Override
    public Flux<String> chat(Long memoryId, String message) {
        if (memoryId == null) {
            memoryId = System.currentTimeMillis();
        }
        log.info("开始对话, memoryId: {}, message: {}", memoryId, message);
        return csrAgent.chat(memoryId, message);
    }

    /**
     * 获取所有历史会话列表
     *
     * @return 历史会话列表
     */
    @Override
    public Result<List<ChatMessages>> getAllSessions() {
        try {
            List<ChatMessages> sessions = mongoChatMemoryStore.getAllSessions();
            return Result.success(sessions);
        } catch (Exception e) {
            log.error("获取历史会话列表失败", e);
            return Result.error("获取历史会话列表失败: " + e.getMessage());
        }
    }

    /**
     * 删除指定会话
     *
     * @param memoryId 对话唯一标识
     * @return 操作结果
     */
    @Override
    public Result<String> deleteSession(Long memoryId) {
        try {
            mongoChatMemoryStore.deleteMessages(memoryId);
            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("删除会话失败, memoryId: {}", memoryId, e);
            return Result.error("删除会话失败: " + e.getMessage());
        }
    }

    /**
     * 更新会话标题
     *
     * @param memoryId 对话唯一标识
     * @param title    新的会话标题
     * @return 操作结果
     */
    @Override
    public Result<String> updateSessionTitle(Long memoryId, String title) {
        try {
            mongoChatMemoryStore.updateSessionTitle(memoryId, title);
            return Result.success("更新标题成功");
        } catch (Exception e) {
            log.error("更新会话标题失败, memoryId: {}", memoryId, e);
            return Result.error("更新会话标题失败: " + e.getMessage());
        }
    }

    /**
     * 创建新的对话会话
     *
     * @return 新会话的 memoryId
     */
    @Override
    public Result<Long> createNewSession() {
        try {
            Long memoryId = System.currentTimeMillis();
            return Result.success(memoryId);
        } catch (Exception e) {
            log.error("创建新会话失败", e);
            return Result.error("创建新会话失败: " + e.getMessage());
        }
    }
}
