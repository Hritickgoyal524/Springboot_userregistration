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
    private final String ACCOUNT_SSID="";// SSID of Twillo account
    private final String AUTH_Token=""; // Auth token of twillo account
    private final String fromNumber=""; // phone number from twillo account
    @Autowired
    Otpservice otpservice;
    @Autowired
    private SimpMessagingTemplate webSocket;
    private final String Topic_destination = "sms";


    //For sending OTP
    public ResponseEntity<?>Send(SMSModel sm){
       try {
            Twilio.init(ACCOUNT_SSID, AUTH_Token); // To initialize twillo
            int number = otpservice.generateOTP(sm.getPhoneNo()); // generating otp
            String mssg = "Your StoneAdda Mobile Verification Code is: " + number + " -Team StoneAdda";
            PhoneNumber ph = new PhoneNumber(sm.getPhoneNo());

            Message message = Message.creator(new PhoneNumber(sm.getPhoneNo()), new PhoneNumber(fromNumber), mssg).create();

            webSocket.convertAndSend(Topic_destination, getTimeStamp() + ":SMS has been sent!" + sm.getPhoneNo()); // sending OTP

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