package com.example.demo.src.email;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailServiceForSendMail {

    private JavaMailSender mailSender;

    public void joinEmail(String email, int certifiedIdCode){
        String setFrom = "koc2827@gmail.com";
        String toMail = email;
        String title = "[라이징캠프-오늘의집] 인증코드 안내";

        String content =
                "<a href=\"https://ohou.se\" rel=\"noreferrer noopener\" target=\"_blank\">" +
                        "<img src=\"https://image.ohou.se/i/bucketplace-v2-development/uploads/cards/162642310342712182.png\" style=\"width: 74px;\" alt=\"오늘의집\" loading=\"lazy\"> </a>" +
                        "<div style=\"font-size: 18px; font-weight: 700; margin-bottom: 10px; margin-top: 60px;\"> 인증코드를 확인해주세요. </div>" +
                        "<span style=\"font-size: 32px; line-height: 42px; font-weight: 700; display: block; margin-top: 6px;\"> " + certifiedIdCode + "</span>" +
                        "<div style=\"margin-top: 60px; margin-bottom: 40px; line-height: 28px;\">" +
                        "<div style=\"display:inline-block;\">이메일 인증 절차에 따라 이메일 인증코드를 발급해드립니다.<br>인증코드는 이메일 발송 시점으로부터 3분동안 유효합니다.</div>";

        sendMail(setFrom, toMail, title, content);
    }

    public void sendMail(String setFrom, String toMail, String title, String content) {
        System.out.println("setFrom = " + setFrom);
        System.out.println("toMail = " + toMail);
        System.out.println("title = " + title);
        System.out.println("content = " + content);
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8"); // true 매개값을 전달하면 multipart 형식의 메세지 전달이 가능.문자 인코딩 설정도 가능하다
            helper.setFrom(setFrom);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content, true); //true가 html형식으로 전송, false면 단순 텍스트
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
