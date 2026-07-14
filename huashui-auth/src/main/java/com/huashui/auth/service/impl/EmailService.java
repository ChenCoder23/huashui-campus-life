package com.huashui.auth.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @PostConstruct
    public void test(){
        System.out.println("mail username = " + from);
    }

    public void sendVerifyCode(String toEmail, String code) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(toEmail);
            helper.setSubject("【华水校园生活】登录验证码");

            String content = """
                    <div style="font-family: Arial, sans-serif; padding: 20px;">
                        <h2>华水校园生活(本项目只作为学习使用！如有疑问联系该项目的开发者：陈会闯) - 登录验证</h2>
                        <p>您的验证码是：</p>
                        <h1 style="color: #1a73e8; letter-spacing: 5px;">%s</h1>
                        <p style="color: #999;">验证码5分钟内有效，请勿泄露给他人。</p>
                    </div>
                    """.formatted(code);

            helper.setText(content, true); // true 表示发送HTML内容

            mailSender.send(message);
            log.info("验证码邮件发送成功，收件人: {}", toEmail);
        } catch (MessagingException e) {
            log.error("验证码邮件发送失败，收件人: {}", toEmail, e);
            throw new RuntimeException("邮件发送失败，请稍后重试");
        }
    }




}