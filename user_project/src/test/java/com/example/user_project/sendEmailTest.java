package com.example.user_project;

import com.example.user_project.service.UserService;
import com.example.user_project.serviceimpl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
public class sendEmailTest {

    @Mock
    JavaMailSender javaMailSender;

    @InjectMocks
    UserServiceImpl userService;

   private SimpleMailMessage expectedMessage;

   @Test
    public void sendEmailTest()
   {

     String to = "Animesh@gmail.com";
     String cc = "Rituja9799@gmail.com";
     String subject = "Regarding saving info";
     String body = "Your info is saved in db";

     expectedMessage = new SimpleMailMessage();
     expectedMessage.setFrom(userService.getFromEmail());
     expectedMessage.setTo(to);
     expectedMessage.setCc(cc);
     expectedMessage.setSubject(subject);
     expectedMessage.setText(body);

     userService.sendEmail(to,cc,subject,body);



       Mockito.verify(javaMailSender,Mockito.times(1)).send(expectedMessage);



   }





}
