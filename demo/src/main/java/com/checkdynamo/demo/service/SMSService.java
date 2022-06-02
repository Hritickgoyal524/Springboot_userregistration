package com.checkdynamo.demo.service;

import com.checkdynamo.demo.model.SMSModel;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class SMSService{
    private final String ACCOUNT_SSID="AC9ec87f8e1bb1b9681b7ad52d9545d8bd";
    private final String AUTH_Token="21fb7651f794df17195a3d8cd4a4aa8c";
    private final String fromNumber="+19403735365";
    @Autowired
    Otpservice otpservice;
    @Autowired
    private SimpMessagingTemplate webSocket;
    private final String Topic_destination = "sms";
    public ResponseEntity<?>Send(SMSModel sm){
       try {
            Twilio.init(ACCOUNT_SSID, AUTH_Token);
            int number = otpservice.generateOTP(sm.getPhoneNo());
            String mssg = "Your StoneAdda Mobile Verification Code is: " + number + " -Team StoneAdda";
            PhoneNumber ph = new PhoneNumber(sm.getPhoneNo());

            Message message = Message.creator(new PhoneNumber(sm.getPhoneNo()), new PhoneNumber(fromNumber), mssg).create();

            webSocket.convertAndSend(Topic_destination, getTimeStamp() + ":SMS has been sent!" + sm.getPhoneNo());

        }
        catch(Exception e){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please try again later!");
        }
       return ResponseEntity.ok().body("OTP send successfully!");
    }
    public void receive(MultiValueMap<String, String> smscallback) {
    }
    private String getTimeStamp(){
        return DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss").format(LocalDateTime.now());
    }
}