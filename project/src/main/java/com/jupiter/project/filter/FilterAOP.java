package com.jupiter.project.filter;

import com.jupiter.project.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
@Aspect
@Configuration
@Component
@Slf4j
public class FilterAOP {

    @Before("within(@org.springframework.web.bind.annotation.RestController *) ")
    public void hasAuthorities() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String header = request.getHeader("x-role");

        if (!header.equals("ADMIN")) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED.value(), "Dont have permission");
        }

    }
}
