package com.example.project.controller;

import com.example.project.dto.UserDto;
import com.example.project.service.OAuthService;
import com.example.project.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.project.service.UserService.logger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {

    private final OAuthService oAuthService;
    private final UserService userService;

    private final Map<String, Boolean> usedAuthorizationCodes = new ConcurrentHashMap<>();

    @GetMapping("/kakao")
    public synchronized void kakaoCallback(@RequestParam String code, HttpServletResponse response, HttpServletRequest request) throws IOException {
        if (usedAuthorizationCodes.containsKey(code)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Authorization code has already been used");
            return;
        }

        try {
            usedAuthorizationCodes.put(code, true);
            String accessToken = oAuthService.getKakaoAccessToken(code);
            logger.info("Received Access Token: {}", accessToken);

            HashMap<String, Object> userInfo = oAuthService.getUserInfo(accessToken);
            String email = (String) userInfo.get("email");
            String nickname = (String) userInfo.get("nickname");

            // userID를 조건문 밖에서 선언
            String userID;

            // 이메일이 없는 경우 새로운 userID 생성
            if (!userService.checkEmail(email)) {
                userID = "K" + generateRandomUserID();
                while (userService.checkUserID(userID)) {
                    userID = "K" + generateRandomUserID();
                }

                // 세션에 필요한 사용자 정보 저장
                HttpSession session = request.getSession();
                session.setAttribute("userID", userID);
                session.setAttribute("userName", nickname);
                session.setAttribute("email", email);
                session.setAttribute("userPassword", userID);
                session.setAttribute("accessToken", accessToken);

                logger.info("userID: {}", userID != null ? userID : "null");
                logger.info("userName: {}",nickname != null ? nickname : "null");
                logger.info("email: {}", email != null ? email : "null");
                logger.info("userPassword: {}", userID != null ? userID : "null");

            } else {
                // 이미 존재하는 이메일에 대해 userID 가져오기
                userID = userService.findByEmail(email).getUserID();
            }

            // Redirect URI with user info parameters
            String redirectUri = "myapp://oauth/callback";
            redirectUri += "?token=" + URLEncoder.encode(accessToken, "UTF-8");
            redirectUri += "&userID=" + URLEncoder.encode(userID, "UTF-8");
            redirectUri += "&userName=" + URLEncoder.encode(nickname, "UTF-8");
            redirectUri += "&email=" + URLEncoder.encode(email, "UTF-8");
            redirectUri += "&kakao=true";  // 카카오 로그인 여부 추가

            response.sendRedirect(redirectUri); // 리디렉션 수행

        } catch (RuntimeException e) {
            usedAuthorizationCodes.remove(code);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to retrieve access token");
        }
    }




    private String generateRandomUserID() {
        Random random = new Random();
        int randomNumber = random.nextInt(9000000) + 1000000;  // 1000000부터 9999999 사이의 7자리 숫자 생성
        return String.valueOf(randomNumber);
    }


}
