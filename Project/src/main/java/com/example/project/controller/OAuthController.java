package com.example.project.controller;

import com.example.project.service.OAuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.io.IOException;  // 추가된 import
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.project.service.UserService.logger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {

    private final OAuthService oAuthService;

    // 인가 코드 재사용 방지를 위한 메모리 저장소 (실제 구현에서는 DB나 Redis 사용 추천)
    private final Map<String, Boolean> usedAuthorizationCodes = new ConcurrentHashMap<>();

    @GetMapping("/kakao")
    public synchronized void kakaoCallback(@RequestParam String code, HttpServletResponse response) throws IOException {
        // 인가 코드 재사용 방지 처리
        if (usedAuthorizationCodes.containsKey(code)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Authorization code has already been used");
            return;
        }

        try {
            // 인가 코드를 사용한 것으로 기록 (중복 방지)
            usedAuthorizationCodes.put(code, true);

            // 인가 코드를 이용해 액세스 토큰을 가져옴
            String accessToken = oAuthService.getKakaoAccessToken(code);

            logger.info("Received Access Token: {}", accessToken);

            // 액세스 토큰을 포함한 URI로 리디렉션
            String redirectUri = "myapp://oauth/callback?token=" + accessToken;
            response.sendRedirect(redirectUri);

        } catch (RuntimeException e) {
            // 인가 코드 사용 실패 시 기록을 삭제하여 다음 시도를 가능하게 함
            usedAuthorizationCodes.remove(code);

            // 일반적인 오류 처리
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to retrieve access token");
        }
    }
}
