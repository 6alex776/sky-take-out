package com.smartdine.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 服务端
 * 用于实时推送订单状态变更、新订单提醒等消息
 */
@Component
@Slf4j
@ServerEndpoint("/ws/{sid}")
public class WebSocketServer {

    /**
     * 存储所有连接的会话，使用 ConcurrentHashMap 保证线程安全
     */
    private static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 存储用户ID与sessionID的映射关系，用于向特定用户推送消息
     */
    private static final Map<Long, String> userSessionMap = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     *
     * @param session WebSocket会话
     * @param sid     会话ID
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        log.info("WebSocket连接建立: sid={}", sid);
        sessionMap.put(sid, session);
    }

    /**
     * 收到客户端消息后调用的方法
     * 支持的消息类型：
     * - heartbeat: 心跳检测
     * - register: 注册用户ID，建立用户与session的映射
     * - order_status: 查询订单状态
     *
     * @param message 客户端发送的消息
     * @param session WebSocket会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自客户端的消息: {}", message);
        try {
            JSONObject jsonObject = JSON.parseObject(message);
            String type = jsonObject.getString("type");

            switch (type) {
                case "heartbeat":
                    // 心跳响应
                    sendMessage(session, createMessage("heartbeat_ack", "pong"));
                    break;
                case "register":
                    // 注册用户ID
                    Long userId = jsonObject.getLong("userId");
                    if (userId != null) {
                        String sid = getSessionId(session);
                        userSessionMap.put(userId, sid);
                        log.info("用户注册成功: userId={}, sid={}", userId, sid);
                        sendMessage(session, createMessage("register_ack", "注册成功"));
                    }
                    break;
                case "order_status":
                    // 查询订单状态
                    Long orderId = jsonObject.getLong("orderId");
                    if (orderId != null) {
                        // 可以在这里查询订单状态并返回
                        log.info("查询订单状态: orderId={}", orderId);
                    }
                    break;
                default:
                    log.warn("未知的消息类型: {}", type);
            }
        } catch (Exception e) {
            log.error("处理WebSocket消息失败", e);
        }
    }

    /**
     * 连接关闭调用的方法
     *
     * @param session WebSocket会话
     * @param sid     会话ID
     */
    @OnClose
    public void onClose(Session session, @PathParam("sid") String sid) {
        log.info("WebSocket连接关闭: sid={}", sid);
        sessionMap.remove(sid);
        // 清理用户映射
        userSessionMap.values().remove(sid);
    }

    /**
     * 发生错误时调用的方法
     *
     * @param session WebSocket会话
     * @param error   错误信息
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket发生错误: ", error);
    }

    /**
     * 向指定客户端发送消息
     *
     * @param session WebSocket会话
     * @param message 消息内容
     */
    private void sendMessage(Session session, String message) {
        try {
            if (session != null && session.isOpen()) {
                session.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            log.error("发送WebSocket消息失败", e);
        }
    }

    /**
     * 向指定会话ID发送消息
     *
     * @param sid     会话ID
     * @param message 消息内容
     */
    public void sendToSession(String sid, String message) {
        Session session = sessionMap.get(sid);
        if (session != null && session.isOpen()) {
            sendMessage(session, message);
        } else {
            log.warn("会话不存在或已关闭: sid={}", sid);
        }
    }

    /**
     * 向指定用户发送消息
     *
     * @param userId  用户ID
     * @param message 消息内容
     */
    public void sendToUser(Long userId, String message) {
        String sid = userSessionMap.get(userId);
        if (sid != null) {
            sendToSession(sid, message);
        } else {
            log.warn("用户未在线: userId={}", userId);
        }
    }

    /**
     * 向所有客户端发送消息
     *
     * @param message 消息内容
     */
    public void sendToAll(String message) {
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            if (session != null && session.isOpen()) {
                sendMessage(session, message);
            }
        }
    }

    /**
     * 向所有商家端客户端广播消息
     *
     * @param message 消息内容
     */
    public void broadcastToMerchant(String message) {
        sendToAll(message);
    }

    /**
     * 发送订单状态变更通知
     *
     * @param orderId   订单ID
     * @param status    订单状态
     * @param statusName 状态名称
     */
    public void sendOrderStatusNotification(Long orderId, Integer status, String statusName) {
        JSONObject message = new JSONObject();
        message.put("type", "order_status_change");
        message.put("orderId", orderId);
        message.put("status", status);
        message.put("statusName", statusName);
        message.put("timestamp", System.currentTimeMillis());
        sendToAll(message.toJSONString());
        log.info("发送订单状态变更通知: orderId={}, status={}", orderId, statusName);
    }

    /**
     * 发送新订单提醒
     *
     * @param orderId 订单ID
     * @param amount  订单金额
     */
    public void sendNewOrderNotification(Long orderId, Double amount) {
        JSONObject message = new JSONObject();
        message.put("type", "new_order");
        message.put("orderId", orderId);
        message.put("amount", amount);
        message.put("timestamp", System.currentTimeMillis());
        sendToAll(message.toJSONString());
        log.info("发送新订单提醒: orderId={}, amount={}", orderId, amount);
    }

    /**
     * 发送营业数据更新通知
     *
     * @param turnover 营业额
     * @param orderCount 订单数
     */
    public void sendBusinessDataUpdate(Double turnover, Integer orderCount) {
        JSONObject message = new JSONObject();
        message.put("type", "business_data_update");
        message.put("turnover", turnover);
        message.put("orderCount", orderCount);
        message.put("timestamp", System.currentTimeMillis());
        sendToAll(message.toJSONString());
    }

    /**
     * 创建标准格式的消息
     *
     * @param type    消息类型
     * @param content 消息内容
     * @return JSON格式的消息字符串
     */
    private String createMessage(String type, Object content) {
        JSONObject message = new JSONObject();
        message.put("type", type);
        message.put("content", content);
        message.put("timestamp", System.currentTimeMillis());
        return message.toJSONString();
    }

    /**
     * 获取会话ID
     *
     * @param session WebSocket会话
     * @return 会话ID
     */
    private String getSessionId(Session session) {
        for (Map.Entry<String, Session> entry : sessionMap.entrySet()) {
            if (entry.getValue().equals(session)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * 获取当前在线客户端数量
     *
     * @return 在线客户端数量
     */
    public int getOnlineCount() {
        return sessionMap.size();
    }
}
