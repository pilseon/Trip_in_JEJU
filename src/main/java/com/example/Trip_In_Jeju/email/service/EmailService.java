package com.example.Trip_In_Jeju.email.service;


import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private  final JavaMailSender mailSender;

    public void send(String to, String subject, String body) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(to); // 메일 수신자
            mimeMessageHelper.setSubject(subject); // 메일 제목
            mimeMessageHelper.setText(body, true); // 메일 본문 내용, HTML 여부
            mailSender.send(mimeMessage); // 메일 발송
        } catch (MailAuthenticationException e) {
            log.error("메일 인증 실패: {}", e.getMessage());
            throw e; // 예외를 다시 던져 호출자에게 알림
        } catch (MailException e) {
            log.error("메일 전송 실패: {}", e.getMessage());
            throw e; // 예외를 다시 던져 호출자에게 알림
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendVerificationCode(String to, String subject, String body, String verificationCode) {
        String bodyWithCode = body + "\n인증 코드: " + verificationCode;
        send(to, subject, bodyWithCode);
    }

    public void sendPasswordResetEmail(String to, String resetLink) {
        String subject = "비밀번호 재설정 안내";
        String body = "비밀번호를 재설정하려면 아래 링크를 클릭하세요:<br><br>" +
                "<a href=\"" + resetLink + "\">비밀번호 재설정</a><br><br>" +
                "이 링크는 1시간 동안 유효합니다.";
        send(to, subject, body);
    }
}
