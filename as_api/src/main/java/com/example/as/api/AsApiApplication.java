package com.example.as.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@MapperScan("com.example.as.api")
@EnableSwagger2
@SpringBootApplication
public class AsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsApiApplication.class, args);
    }

}
