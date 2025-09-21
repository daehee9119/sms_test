package com.allane.bp.smstest.api;

import io.awspring.cloud.sns.sms.SmsMessageAttributes;
import io.awspring.cloud.sns.sms.SmsType;
import io.awspring.cloud.sns.sms.SnsSmsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SmsController  {

    private final SnsSmsTemplate snsSmsTemplate;

    @PostMapping("/sms")
    public void sendSms(@RequestBody SmsTestRequestDto smsTestRequestDto) {
        SmsMessageAttributes smsMessageAttributes = new SmsMessageAttributes();
        smsMessageAttributes.setSmsType(SmsType.TRANSACTIONAL);
        smsMessageAttributes.setSenderID(smsTestRequestDto.senderId());

        String message = "test message";
        snsSmsTemplate.send(smsTestRequestDto.phoneNumber(), message, smsMessageAttributes);
        log.info("Test SMS - senderID:{}, phonenumber:{}", smsTestRequestDto.senderId(), smsTestRequestDto.phoneNumber());
    }

}
