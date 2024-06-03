package com.example.project.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Component
public class LoginClient {

    @Autowired
    private RestTemplate restTemplate;

    public void login(String userID, String userPassword) {
        String url = "http://127.0.0.1:8080/api/login";

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("userID", userID); // 수정된 부분
        requestBody.add("userPassword", userPassword);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        System.out.println(response.toString());
    }
}
