package com.zyx.user.controller;

import com.zyx.api.controller.user.HelloControllerApi;
import com.zyx.grace.result.GraceJSONResult;
import com.zyx.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@RefreshScope                // 动态刷新的注解
public class HelloController implements HelloControllerApi {

    final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private RedisOperator redis;

    @Value("${server.port}")
    private String port;


    @Value("${alibaba.user.name}")
    private String name;

    @Value("${alibaba.user.password}")
    private String password;


    @GetMapping("/getConfigInfo")
    public Object getConfigInfo() {

        return "name:" + name + "password:" + password;
    }


    //    Swagger2 文档生成工具
    @GetMapping("/hello")
    public Object hello() {

        logger.debug("debug: hello~");
        logger.info("info: hello~");
        logger.warn("warn: hello~");
        logger.error("error: hello~");

//        return "hello";
//        return IMOOCJSONResult.ok();
//        return IMOOCJSONResult.ok("hello");
//        return IMOOCJSONResult.errorMsg("您的信息有误~！");

        System.out.println(port);

        return GraceJSONResult.ok();
    }

    @GetMapping("/redis")
    public Object redis() {
        redis.set("age", "18");
        return GraceJSONResult.ok(redis.get("age"));
    }

}
