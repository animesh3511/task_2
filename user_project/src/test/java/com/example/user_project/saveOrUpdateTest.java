package com.example.user_project;

import com.example.user_project.model.User;
import com.example.user_project.model.request.UserRequest;
import com.example.user_project.repository.UserRepository;
import com.example.user_project.service.UserService;
import com.example.user_project.serviceimpl.UserServiceImpl;
import com.lowagie.text.DocumentException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.mail.MessagingException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class saveOrUpdateTest {


    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void setUp()
    {

        user = new User();
        user.setUserId((long) 1);
        user.setUserName("John_Cena");
        user.setFirstName("John");
        user.setLastName("Cena");
        user.setCity("amshd");
        user.setEmail("dhjhfrejgk");
        user.setPassword("mdkjhfuerj");
        user.setContact("87665");
        user.setIsActive(true);
        user.setIsDeleted(false);


    }

    @Test
    public void saveOrUpdateTest() throws MessagingException, DocumentException {
        UserRequest userRequest = new UserRequest();
        userRequest.setUserId((long) 3254);
        userRequest.setUserName("jkhjFRT_");
        userRequest.setFirstName("jhtdfg");
        userRequest.setLastName("jhytd");
        userRequest.setCity("jhytffhhtt");
        userRequest.setEmail("hrishi@gmail.com");
        userRequest.setPassword("kjuYT98#$");
        userRequest.setContact("8765678767");

        Mockito.when(userRepository.existsById((long) 3254)).thenReturn(false);
        //Mockito.when(userRepository.existsById((long) 3254)).thenReturn(true);
        //Mockito.when(userRepository.findById((long) 3254)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.existsByEmail("hrishi@gmail.com")).thenReturn(false);

         Object result  = userService.saveOrUpdate(userRequest);


         Mockito.verify(userRepository,Mockito.times(1)).save(Mockito.any(User.class));

       // Assertions.assertEquals("user updated",result);
          Assertions.assertEquals("user saved",result);


     //saveOrUpdateTest() method ends here
    }


//class ends here
}
