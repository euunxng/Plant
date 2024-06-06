package com.example.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public String sendVerificationCode(String email) {
        String verificationCode = generateVerificationCode();
        sendEmail(email, "F_plant 인증번호 전송", "인증번호는 " + verificationCode);
        return verificationCode;
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        int randomNum = random.nextInt((max - min) + 1) + min;
        return String.valueOf(randomNum);
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("hongdora06@naver.com"); // 네이버 계정 이메일 주소
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
