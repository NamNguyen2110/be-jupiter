package com.jupiter.gateway;

import com.jupiter.common.dto.RealmsResponse;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Data
@Component
@Slf4j
@RequiredArgsConstructor
@Getter
public class Initializing implements InitializingBean {

    private final ResourceLoader resourceLoader;

    private final RestTemplate restTemplate;

    @Value("${keycloak.auth-server-url}")
    private String baseKeycloakUrl;

    @Value("${keycloak.realm}")
    private String realms;

    public static String pubicKey = null;


    @Override
    public void afterPropertiesSet() {
        ResponseEntity<RealmsResponse> response = restTemplate.getForEntity(baseKeycloakUrl + "/realms/" + realms, RealmsResponse.class);
        if (response.getBody() != null) {
            pubicKey = response.getBody().getPublicKey();
        }
    }
}
