package com.tao.taoaicodemother;

import dev.langchain4j.community.store.embedding.redis.spring.RedisEmbeddingStoreAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {RedisEmbeddingStoreAutoConfiguration.class})
@MapperScan("com.tao.taoaicodemother.mapper")
public class TaoAiCodeMotherApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaoAiCodeMotherApplication.class, args);
    }

}
