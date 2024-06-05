package com.example.project.dto;

public class EmailRequest {
    private String recipientEmail; // 수신자 이메일 주소
    private String subject; // 이메일 제목
    private String content; // 이메일 내용

    // 생성자
    public EmailRequest() {
    }

    public EmailRequest(String recipientEmail, String subject, String content) {
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.content = content;
    }

    // 게터 및 세터
    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

