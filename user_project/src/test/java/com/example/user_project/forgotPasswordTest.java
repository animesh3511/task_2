package com.example.user_project;

import com.example.user_project.model.User;
import com.example.user_project.repository.UserRepository;
import com.example.user_project.serviceimpl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class forgotPasswordTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    private User user;

    String resultToken;

    @BeforeEach
    void setUp()
    {

     user = new User();
     user.setUserId((long)45);
     user.setUserName("Animesh_Suryawanshi");
     user.setFirstName("Animesh");
     user.setLastName("Suryawanshi");
     user.setEmail("Animesh@gmail.com");
     user.setPassword("Animesh@$100$");
     user.setCity("Karad");
     user.setContact("9878563758");
     user.setIsActive(true);
     user.setIsDeleted(false);
     user.setToken(user.getToken());
        System.out.println("user token in Before Each :" +user.getToken());
     user.setTokenCreationTime(LocalDateTime.now());

    }

    @Test
    public void forgotPasswordTest()
    {

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);

        //here we can send any email we want bcoz in above line we have taken an arg "Mockito.anyString()"
        resultToken = (String) userService.forgotPassword("Omkar@gmail.com");

        System.out.println("User Token in Object resultToken : "+resultToken);

        //eth user.getToken() la value ahe bcoz API mdhe aapn user.setToken(generateToken()) ha call dilela ahe
        //generateToken() hi method userServiceImpl mdhech khali lihili ahe,tyamule ith value retrieve hot ahe
        Assertions.assertEquals(user.getToken(),resultToken);

        System.out.println("Expected User Token : "+user.getToken());


        Mockito.verify(userRepository,Mockito.times(1)).save(user);
        //here we are verifying after the call to forgotPassword() API has been made.so here, we have to send same
        //email as that of which we have send to forgotPassword() API above.In this case it is "Omkar@gmail.com"
        Mockito.verify(userRepository,Mockito.times(1)).findByEmail("Omkar@gmail.com");

    }


    @Test
    public void resetPasswordTest()
    {
        Mockito.when(userRepository.findByToken(Mockito.anyString())).thenReturn(user);

     Object result = userService.resetPassword("ttttt","newPassword");

       Assertions.assertEquals("Your password is updated succesfully",result);

       Mockito.verify(userRepository,Mockito.times(1)).findByToken("ttttt");

       Assertions.assertNull(user.getToken());

       Assertions.assertNull(user.getTokenCreationTime());

    }




}
