package com.zyx.user.controller;

import com.zyx.api.controller.user.HelloControllerApi;
import com.zyx.grace.result.GraceJSONResult;
import com.zyx.grace.result.IMOOCJSONResult;
import com.zyx.grace.result.ResponseStatusEnum;
import com.zyx.utils.RedisOperator;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController implements HelloControllerApi {

    final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private RedisOperator redis;

//    Swagger2 文档生成工具
    public Object hello() {

        logger.debug("debug: hello~");
        logger.info("info: hello~");
        logger.warn("warn: hello~");
        logger.error("error: hello~");

//        return "hello";
//        return IMOOCJSONResult.ok();
//        return IMOOCJSONResult.ok("hello");
//        return IMOOCJSONResult.errorMsg("您的信息有误~！");
        return GraceJSONResult.ok();
    }

    @GetMapping("/redis")
    public Object redis() {
        redis.set("age", "18");
        return GraceJSONResult.ok(redis.get("age"));
    }

}
