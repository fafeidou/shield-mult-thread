package com.shield;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.shield.mapper")
public class ShieldApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShieldApplication.class, args);
    }

}
