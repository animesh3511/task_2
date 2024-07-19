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

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class findByIdTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;


    private User user;

    @BeforeEach
    void setUp()
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
    public void findByIdTest() throws Exception {


        Mockito.when(userRepository.findById((long)35)).thenReturn(Optional.of(user));

        Object result = userService.findById((long)35);

        Assertions.assertEquals(user,result);

        Mockito.verify(userRepository,Mockito.times(1)).findById((long)35);


    }



}
