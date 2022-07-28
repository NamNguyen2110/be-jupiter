package com.jupiter.gateway.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jupiter.common.dto.RoleResponse;
import com.jupiter.gateway.utils.JsonUtils;
import com.jupiter.gateway.Initializing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class GatewayFilter implements GlobalFilter {

    @Autowired
    private JsonUtils jsonUtils;

    @Value("${keycloak-client.client-id}")
    private String clientId;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        DecodedJWT decodedJWT = verifyToken(exchange);
        Map<String, Claim> map = decodedJWT.getClaims();
        Map<String, Object> objectMap = map.get("resource_access").asMap();
        List<String> roles = getRoleFromAccessToken(objectMap, clientId);
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            HttpHeaders responseHeader = response.getHeaders();
            responseHeader.add("x-role", roles.get(0));
            log.info(String.valueOf(response.getHeaders()));
            if (response.getStatusCode().value() >= 400) responseHeader.remove("Cache-Control");
        }));
    }

    private DecodedJWT verifyToken(ServerWebExchange exchange) {
        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) getPublicKey(Initializing.pubicKey), null);
        DecodedJWT decodedJWT = JWT.decode(getAccessToken(exchange));
        algorithm.verify(decodedJWT);
        return decodedJWT;
    }

    private List<String> getRoleFromAccessToken(Map<String, Object> objectMap, String clientId) {
        log.info(jsonUtils.convertObjToString(objectMap.get(clientId)));
        return jsonUtils.convertStringToObject(jsonUtils.convertObjToString(objectMap.get(clientId)), RoleResponse.class).getRoles();
    }

    public static PublicKey getPublicKey(String publicKey) {
        PublicKey pubKey = null;
        try {
            byte[] publicBytes = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            pubKey = keyFactory.generatePublic(keySpec);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return pubKey;
    }

    public static String getAccessToken(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        List<String> token = headers.get("Authorization");
        if (token != null && !token.isEmpty()) {
            return token.get(0).substring(7);
        } else {
            return "";
        }
    }

}
