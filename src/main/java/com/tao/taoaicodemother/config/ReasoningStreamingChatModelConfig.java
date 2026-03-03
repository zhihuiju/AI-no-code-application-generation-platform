package com.tao.taoaicodemother.config;

import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "langchain4j.open-ai.chatmodel")
@Data
public class ReasoningStreamingChatModelConfig {
    private String baseUrl;

    private String apiKey;

    @Bean
    public StreamingChatModel reasoningStreamingChatModel(){
        //为了方便测试临时修改
        final String modelName = "deepseek-chat";
        final int maxTokens = 8192;
        //生成环境使用
//        final String modelName = "deepseek-reasoner";
//        final int maxTokens = 40000;
        return OpenAiStreamingChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(modelName)
                .maxTokens(maxTokens)
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
