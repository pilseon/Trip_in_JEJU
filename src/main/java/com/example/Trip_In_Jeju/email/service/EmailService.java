package com.example.Trip_In_Jeju.email.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    public void send(String to, String subject, String body) throws MessagingException {
        log.info("메일 전송 시작: 수신자 = {}, 제목 = {}", to, subject);

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(to); // 메일 수신자
            mimeMessageHelper.setSubject(subject); // 메일 제목
            mimeMessageHelper.setText(body, true); // 메일 본문 내용 , HTML 여부

            mailSender.send(mimeMessage); // 메일 발송
            log.info("메일 전송 성공");

        } catch (MessagingException e) {
            log.error("MessagingException 발생: ", e);
            Throwable cause = e.getCause();
            while (cause != null) {
                log.error("원인: ", cause);
                cause = cause.getCause();
            }
            throw e;
        } catch (Exception e) {
            log.error("Exception 발생: ", e);
            throw new RuntimeException(e);
        }
    }

    public void sendVerificationCode(String to, String subject, String body, String verificationCode) throws MessagingException {
        String bodyWithCode = body + "\n인증 코드: " + verificationCode;
        send(to, subject, bodyWithCode);
    }

    public void sendPasswordResetEmail(String to, String resetLink) {
        String subject = "비밀번호 재설정 안내";
        String body = "비밀번호를 재설정하려면 아래 링크를 클릭하세요:<br><br>" +
                "<a href=\"" + resetLink + "\">비밀번호 재설정</a><br><br>" +
                "이 링크는 1시간 동안 유효합니다.";
        try {
            send(to, subject, body);
        } catch (MessagingException e) {
            throw new RuntimeException("메일 전송 중 문제가 발생했습니다.", e);
        }
    }
}