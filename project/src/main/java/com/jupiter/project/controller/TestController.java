package com.jupiter.project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/project")
@Slf4j
public class TestController {
    @GetMapping("/test")
    public String test(@RequestHeader(value = "x-role",required = false) String role) {
        log.info("x-role: {}", role);
        return "abc";
    }
}
