package com.jupiter.project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/project")
@Slf4j
public class TestController {
    @GetMapping("/test")
    public String test(HttpServletRequest request) {
        log.info("x-role: {}", request.getHeader("x-role"));
        return "abc";
    }

    @PostMapping(value = "/aop")
    public String testAop(@RequestHeader String role){
        return "You are admin";
    }
}
