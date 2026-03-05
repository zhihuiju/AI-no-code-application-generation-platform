package com.tao.taoaicodemother.core.handler;

import com.tao.taoaicodemother.model.entity.User;
import com.tao.taoaicodemother.model.enums.ChatHistoryMessageTypeEnum;
import com.tao.taoaicodemother.service.ChatHistoryService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * 简单文本流处理器
 * 处理HTML和MULTI_FILE 类型的流式响应
 */
@Slf4j
public class SimpleTextStreamHandler {
    /**
     * 处理传统流（HTML , MULTI_FILE）
     * 直接收集完整的文本响应
     * @param originFlux  原始流
     * @param chatHistoryService  聊天记录服务
     * @param appId  应用ID
     * @param longinUser  登录用户
     * @return  处理后的流
     */
    public Flux<String> handle(Flux<String> originFlux,
                               ChatHistoryService chatHistoryService,
                               long appId,
                               User longinUser){
        StringBuilder aiResponseBuilder = new StringBuilder();
        return originFlux
                .map(chunk -> {
            //收集AI响应内容
            aiResponseBuilder.append(chunk);
            return chunk;
        })
                .doOnComplete(() -> {
                    //流式响应完成后，添加AI消息到对话历史
                    String aiResponse = aiResponseBuilder.toString();
                    chatHistoryService.addChatMessage(appId,aiResponse,ChatHistoryMessageTypeEnum.AI.getValue(),longinUser.getId());
                })
                .doOnError(error -> {
                    //如果AI回复失败，也要记录错误信息
                    String errorMessage = "AI回复失败：" + error.getMessage();
                    chatHistoryService.addChatMessage(appId,errorMessage,ChatHistoryMessageTypeEnum.AI.getValue(),longinUser.getId());
                });
    }
}
