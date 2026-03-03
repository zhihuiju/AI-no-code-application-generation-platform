package com.tao.taoaicodemother.ai;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.tao.taoaicodemother.ai.tools.FileWriteTool;
import com.tao.taoaicodemother.config.ReasoningStreamingChatModelConfig;
import com.tao.taoaicodemother.exception.BusinessException;
import com.tao.taoaicodemother.exception.ErrorCode;
import com.tao.taoaicodemother.model.enums.CodeGenTypeEnum;
import com.tao.taoaicodemother.service.ChatHistoryService;
import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Slf4j
public class AiCodeGeneratorServiceFactory {
    @Resource
    private ChatModel chatModel;

    @Resource
    private StreamingChatModel openAiStreamingChatModel;

    @Resource
    private StreamingChatModel reasoningStreamingChatModel;

    @Resource
    private RedisChatMemoryStore redisChatMemoryStore;

    @Resource
    private ChatHistoryService chatHistoryService;

    /**
     * AI 服务实例缓存
     * 缓存策略：
     * - 最大缓存 1000 个实例
     * - 写入后 30 分钟过期
     * - 访问后 10 分钟过期
     */
    private final Cache<String, AiCodeGeneratorService> serviceCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(Duration.ofMinutes(30))
            .expireAfterAccess(Duration.ofMinutes(10))
            .removalListener((key, value, cause) -> {
                log.debug("AI 服务实例被移除，缓存键: {}, 原因: {}", key, cause);
            })
            .build();

    /**
     * 根据appId获取服务
     * @param appId
     * @return
     */
    public AiCodeGeneratorService getAiCodeGeneratorService(long appId){
//        return serviceCache.get(appId,this::createAiCodeGeneratorService);
        return getAiCodeGeneratorService(appId, CodeGenTypeEnum.HTML); //为了兼容老的逻辑,参数HTML并没有实际意义
    }

    /**
     * 根据appId获取服务
     * @param appId
     * @return
     */
    public AiCodeGeneratorService getAiCodeGeneratorService(long appId, CodeGenTypeEnum codeGenType){
        String cacheKey = buildCacheKey(appId, codeGenType);
        return serviceCache.get(cacheKey,key -> createAiCodeGeneratorService(appId, codeGenType));
    }

    /**
     * 创建新的 AI 服务实例 (当从本地缓存中找不到对应的AiService时候)
     */
    private AiCodeGeneratorService createAiCodeGeneratorService(long appId, CodeGenTypeEnum codeGenType) {
        log.info("为 appId: {} 创建新的 AI 服务实例", appId);
        // 根据 appId 构建独立的对话记忆
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory
                .builder()
                .id(appId)
                .chatMemoryStore(redisChatMemoryStore)
                .maxMessages(20)
                .build();
        //从数据库中加载对话历史到记忆中
        chatHistoryService.loadChatHistoryToMemory(appId,chatMemory,20);
        return switch (codeGenType) {
            //HTML 或 多文件 项目生成 使用流式对话模型
            case HTML , MULTI_FILE -> AiServices.builder(AiCodeGeneratorService.class)
                        .chatModel(chatModel)
                        .streamingChatModel(openAiStreamingChatModel)
                        .chatMemory(chatMemory)
                        .build();
            //Vue工程项目生成 使用工具调用和推理模型
            case VUE_PROJECT ->  AiServices.builder(AiCodeGeneratorService.class)
                    .chatModel(chatModel)
                    .streamingChatModel(reasoningStreamingChatModel)
                    .chatMemoryProvider(memoryId -> chatMemory)
                    .tools(new FileWriteTool())
                    //处理工具调用幻觉问题
                    .hallucinatedToolNameStrategy(toolExecutionRequest ->
                            ToolExecutionResultMessage.from(toolExecutionRequest,
                                    "Error: there is no tool called" + toolExecutionRequest.name()))
                    .build();
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR,"不支持的代码生成类型: " + codeGenType.getValue());
            };
    }

    @Bean
    public AiCodeGeneratorService aiCodeGeneratorService() {

//        return AiServices.create(AiCodeGeneratorService.class,chatModel);

//        return AiServices.builder(AiCodeGeneratorService.class)
//                .chatModel(chatModel)
//                .streamingChatModel(openAiStreamingChatModel)
//                .build();

        return getAiCodeGeneratorService(0);
    }

    /**
     * 构造缓存键
     * @param appId
     * @param codeGenType
     * @return
     */
    private String buildCacheKey(long appId , CodeGenTypeEnum codeGenType){
        return appId + "_" + codeGenType.getValue();
    }
}
