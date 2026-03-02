package com.tao.taoaicodemother.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.tao.taoaicodemother.model.dto.chathistory.ChatHistoryQueryRequest;
import com.tao.taoaicodemother.model.entity.ChatHistory;
import com.tao.taoaicodemother.model.entity.User;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

import java.time.LocalDateTime;

/**
 * 对话历史 服务层。
 *
 * @author 韬
 */
public interface ChatHistoryService extends IService<ChatHistory> {

    /**
     * 添加聊天历史
     * @param appId 应用ID
     * @param message 消息
     * @param messageType 消息类型
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean addChatMessage(Long appId , String message , String messageType , Long userId);

    /**
     * 根据应用ID删除对话历史
     * @param appId
     * @return
     */
    boolean deleteByAppId(Long appId);

    /**
     * 分页查询某APP的对话记录
     * @param appId
     * @param pageSize
     * @param lastCreateTime
     * @param loginUser
     * @return
     */
    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize,
                                               LocalDateTime lastCreateTime,
                                               User loginUser);

    /**
     * 加载数据库中的对话历史到内存 （当caffeine 和 redis 中没有时从磁盘取）
     * @param appId 应用ID
     * @param chatMemory 对话记忆
     * @param maxCount 最多加载多少条
     * @return 成功加载的数目
     */
    int loadChatHistoryToMemory(Long appId, MessageWindowChatMemory chatMemory, int maxCount);

    /**
     * 获取查询包装类 构造查询条件
     * @param chatHistoryQueryRequest
     * @return
     */
    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);
}
