package com.checkdynamo.demo.controller;

import com.checkdynamo.demo.model.SMSModel;
import com.checkdynamo.demo.model.TempOtp;
import com.checkdynamo.demo.service.Otpservice;
import com.checkdynamo.demo.service.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class SMSController {

    @Autowired
    SMSService smsService;

    @Autowired
    Otpservice otpservice;


    @PostMapping({"/otpGenerate"})
    public ResponseEntity<?> smsSubmit(@RequestBody SMSModel sm) {

           return smsService.Send(sm);


    }


    @PostMapping("/otpValidate")
    public ResponseEntity<?> verifyOtp(@RequestBody TempOtp sms){
        System.out.println(sms.getOtp());
        System.out.println(otpservice.getotp(sms.getPhoneNo()));
        if(sms.getOtp()== otpservice.getotp(sms.getPhoneNo())) {
            otpservice.clearotp(sms.getPhoneNo());
            return ResponseEntity.ok().body("Phone is verified successfully!");
        }

        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP! Please try again later!");
        }

    }
}