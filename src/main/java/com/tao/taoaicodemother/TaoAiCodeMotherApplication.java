package com.tao.taoaicodemother;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tao.taoaicodemother.mapper")
public class TaoAiCodeMotherApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaoAiCodeMotherApplication.class, args);
    }

}
