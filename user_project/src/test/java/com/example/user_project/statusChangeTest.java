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
public class statusChangeTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    private User user;

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
        user.setContact("8876984567");
        user.setCity("Karad");
        user.setIsActive(true);
        user.setIsDeleted(false);


    }


    @Test
    public void statusActiveToInactive() throws Exception {

        Mockito.when(userRepository.findById((long)50)).thenReturn(Optional.of(user));


        Object result = userService.statusChange((long)50);

        Assertions.assertEquals("user is not active",result);

        Assertions.assertFalse(user.getIsActive());

        Mockito.verify(userRepository,Mockito.times(1)).save(user);


    }


    @Test
    public void statusInactiveToActive() throws Exception {
        user.setIsActive(false);

        Mockito.when(userRepository.findById((long)50)).thenReturn(Optional.of(user));

        Object result = userService.statusChange((long)50);

        Assertions.assertEquals("user is active",result);

        Assertions.assertTrue(user.getIsActive());

        Mockito.verify(userRepository,Mockito.times(1)).save(user);

    }

    @Test
    public void statusChangeIncorrectUserId()
    {

       Exception exception = Assertions.assertThrows(Exception.class,()->{

                             userService.statusChange((long)50);

                             });


      Assertions.assertEquals("user not found",exception.getMessage());

      Mockito.verify(userRepository,Mockito.times(1)).findById((long) 50);
    }

}
