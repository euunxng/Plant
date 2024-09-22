package com.example.project.service;


import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OAuthService {

    private static final Logger logger = LoggerFactory.getLogger(OAuthService.class);

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate;

    public OAuthService() {
        this.restTemplate = new RestTemplate();
    }

    public String getKakaoAccessToken(String code) {
        String reqURL = "https://kauth.kakao.com/oauth/token";

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 요청 바디 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        logger.info("Received authorization code: {}", code);

        // HttpEntity 생성
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            // POST 요청 실행
            ResponseEntity<String> response = restTemplate.exchange(
                    reqURL,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            // 응답 처리
            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                JsonElement element = JsonParser.parseString(responseBody);
                return element.getAsJsonObject().get("access_token").getAsString();
            } else {
                logger.error("Failed to get access token, response code: {}, response: {}",
                        response.getStatusCode(), response.getBody());
                throw new RuntimeException("Failed to get access token: " + response.getBody());
            }
        } catch (Exception e) {
            logger.error("Exception occurred while getting access token2", e);
            throw new RuntimeException("Failed to get access token2", e);
        }
    }
}
