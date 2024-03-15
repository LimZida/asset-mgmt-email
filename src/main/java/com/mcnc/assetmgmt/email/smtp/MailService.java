package com.mcnc.assetmgmt.email.smtp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
/**
 * title : MailService
 *
 * description : 자산 등록 및 노후화, 라이센스 만료 시 SMTP 메일 전송
 *
 * reference :
 *
 *
 *
 * author : 임현영
 * date : 2024.03.12
 **/
@Component
@Slf4j
public class MailService {

    private JavaMailSender javaMailSender;
    private String supportMail;
    private Boolean allowMail;
    private String sendTo;


    @Autowired
    public MailService(@Value("${support.email}") String supportMail, @Value("${mail.enable}") Boolean allowMail,
                       @Value("${mail.sendto}") String sendTo, JavaMailSender javaMailSender){
        this.supportMail = supportMail;
        this.allowMail = allowMail;
        this.sendTo = sendTo;
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String adminMail, String title, String body){
        try{
            if (allowMail){
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setSubject(title);
                helper.setTo(adminMail);
                helper.setText(body);
                helper.setFrom(supportMail);

                getMailLog(helper,adminMail,title,body);

                javaMailSender.send(message);
            }
        }catch (Exception e){
            log.info("##### 메일 에러 발생 : {}",e.getMessage());
        }
    }

    public void getMailLog(MimeMessageHelper helper, String adminMail, String title, String body){
        log.info("---------------------------------------------------");
        log.info("###### 메일 받는 이 : {}", adminMail);
        log.info("###### 메일 제목 : {}", title);
        log.info("###### 메일 내용 : {}", body);
        log.info("---------------------------------------------------");
    }
}
