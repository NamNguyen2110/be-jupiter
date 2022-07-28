package com.jupiter.gateway;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Data
@Component
@Slf4j
public class Initializing implements InitializingBean {
    @Autowired
    ResourceLoader resourceLoader;

    @Override
    public void afterPropertiesSet() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:tesvxcvxcvxct.txt");
        try {
            File file = resource.getFile();
        } catch (FileNotFoundException ex) {
            // TODO: call keycloak to get file
        }
    }
}
