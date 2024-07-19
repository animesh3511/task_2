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
public class changePasswordTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp()
    {

      user = new User();
      user.setUserId((long)34);
      user.setUserName("Animesh_Suryawanshi");
      user.setFirstName("Animesh");
      user.setLastName("Suryawanshi");
      user.setEmail("Animesh@gmail.com");
      user.setPassword("Animesh@123");
      user.setCity("Karad");
      user.setContact("8877665544");
      user.setIsActive(true);
      user.setIsDeleted(false);


    }


    @Test
    public void changePasswordTestSuccess()
    {

        Mockito.when(userRepository.findById((long)50)).thenReturn(Optional.of(user));

        //old password jo vrti user obj mdhe ahe to pathavla ethun tr success hoel
        //jar vegla pathvla tr fail hoel je khalchya test mdhe kely
        Object result = userService.changePassword((long)50,"Animesh@123","Animesh@1234");

        Assertions.assertEquals("user is updated with new password",result);

       // Assertions.assertEquals("Animesh@1234",user.getPassword());

        Mockito.verify(userRepository,Mockito.times(1)).save(user);

    }


    @Test
    public void changePasswordTestFailure()
    {

        Mockito.when(userRepository.findById((long)50)).thenReturn(Optional.of(user));

        //ethun aapn incorrect oldpassword pathavla so that new password set honar nahi
        Object result = userService.changePassword((long)50,"Incorrect Password","Animesh@1234");

        Assertions.assertEquals("oldPassword is incorrect",result);


    }

    @Test
    public void changePasswordTestIncorrectUserId()
    {

       // Mockito.when(userRepository.findById((long) 50)).thenReturn(Optional.of(user));

        Object result = userService.changePassword((long)0,"Animesh@123","Animesh@1234");


        Assertions.assertEquals("userId is incorrect",result);

    }


}
