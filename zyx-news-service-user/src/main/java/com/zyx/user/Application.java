package com.zyx.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
@MapperScan(basePackages = "com.zyx.user.mapper")
@ComponentScan(basePackages = {"com.zyx", "org.n3r.idworker"})
@EnableEurekaClient     // 开启eureka client，注册到server中
@EnableCircuitBreaker   // 开启hystrix的熔断机制
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
