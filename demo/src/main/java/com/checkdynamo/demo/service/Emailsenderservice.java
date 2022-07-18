package com.checkdynamo.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class Emailsenderservice  {
    @Autowired
    private JavaMailSender javaMailSender;
    public void sendMail(String toEmail,String subject,String body){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("goyalhritick@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        javaMailSender.send(message);
        System.out.println("mail send");

    }
}
