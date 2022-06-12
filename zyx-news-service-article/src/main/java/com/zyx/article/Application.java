package com.zyx.article;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.zyx.article.mapper")
@ComponentScan(basePackages = {"com.zyx", "org.n3r.idworker"})
@EnableDiscoveryClient
//@RibbonClient(name = "SERVICE-USER", configuration = MyRule.class)
@EnableFeignClients({"com.zyx"})
@EnableHystrix
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
