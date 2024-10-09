package com.example.project.service;


import com.example.project.domain.User;
import com.example.project.dto.UserDto;
import com.example.project.repository.UserRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private static final Logger logger = LoggerFactory.getLogger(OAuthService.class);

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();
    private final UserRepository userRepository;

    public String getKakaoAccessToken(String code) {
        String reqURL = "https://kauth.kakao.com/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        logger.info("Received authorization code: {}", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    reqURL,
                    HttpMethod.POST,
                    request,
                    String.class
            );

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
            logger.error("Exception occurred while getting access token", e);
            throw new RuntimeException("Failed to get access token", e);
        }
    }

    public HashMap<String, Object> getUserInfo(String accessToken) {
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        HashMap<String, Object> userInfo = new HashMap<>();

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(result.toString());
                JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
                JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

                String nickname = properties.get("nickname").getAsString();
                String email = kakaoAccount.get("email").getAsString();
                logger.info("Parsed user info - Nickname: {}, Email: {}", nickname, email);

                userInfo.put("nickname", nickname);
                userInfo.put("email", email);

            } else {
                logger.error("Failed to fetch user info from Kakao API, response code: {}", responseCode);
                throw new RuntimeException("Failed to fetch user info from Kakao API");
            }
        } catch (IOException e) {
            logger.error("Exception occurred while getting user info", e);
            throw new RuntimeException("Failed to fetch user info", e);
        }
        return userInfo;
    }


}
