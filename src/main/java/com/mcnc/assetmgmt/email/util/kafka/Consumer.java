package com.mcnc.assetmgmt.email.util.kafka;

import com.mcnc.assetmgmt.email.smtp.MailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
public class Consumer {
    private final MailService mailService;
    private CountDownLatch latch;
    private String payload;

    public Consumer(MailService mailService) {
        this.mailService = mailService;
        this.latch = new CountDownLatch(1);
        this.payload = "";
    }


    @KafkaListener(topics = "email", groupId = "group_1")
    public void consume(ConsumerRecord<String, String> consumerRecord) throws Exception {
        getLatch().await(10000, TimeUnit.MILLISECONDS);
        log.info("received payload='{}'", consumerRecord.toString());
        setPayload(consumerRecord.toString());
        latch.countDown();

        mailSend(consumerRecord.value());

    }

    public void mailSend(String text){
        String[] splitText = text.split("~");
        String adminMail = splitText[0];
        String mailContents = splitText[1];

        final StringBuffer contents = new StringBuffer("");
        contents.append("안녕하세요 관리자님!");
        contents.append(System.lineSeparator());
        contents.append(mailContents);
        contents.append(System.lineSeparator());
        contents.append("해당 내용이 맞는지 확인 부탁드립니다.");

        mailService.sendMail(adminMail,"[MCNC] 자산 관리",contents.toString());
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public String getPayload() {
        return payload;
    }

    public String setPayload(String payload) {
        return this.payload=payload;
    }
}
