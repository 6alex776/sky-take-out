package com.smartdine.aicsr;

import com.smartdine.aicsr.bean.ChatMessages;
import com.smartdine.result.Result;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * AI智能客服服务接口
 * 提供对话、历史记录管理等功能
 */
public interface ChatService {

    /**
     * 与AI进行对话（流式输出）
     *
     * @param memoryId 对话唯一标识，用于保持上下文
     * @param message  用户输入的消息
     * @return 流式返回AI的回复
     */
    Flux<String> chat(Long memoryId, String message);

    /**
     * 获取所有历史会话列表
     *
     * @return 历史会话列表
     */
    Result<List<ChatMessages>> getAllSessions();

    /**
     * 删除指定会话
     *
     * @param memoryId 对话唯一标识
     * @return 操作结果
     */
    Result<String> deleteSession(Long memoryId);

    /**
     * 更新会话标题
     *
     * @param memoryId 对话唯一标识
     * @param title    新的会话标题
     * @return 操作结果
     */
    Result<String> updateSessionTitle(Long memoryId, String title);

    /**
     * 创建新的对话会话
     *
     * @return 新会话的 memoryId
     */
    Result<Long> createNewSession();
}
