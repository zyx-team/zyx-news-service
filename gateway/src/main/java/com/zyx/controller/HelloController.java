package com.zyx.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gateway")
public class HelloController {

    @GetMapping("/hello")
    public Object hello() {
        return "hello!!! gateway";
    }

}
