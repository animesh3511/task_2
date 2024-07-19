package com.example.user_project;

import com.example.user_project.model.User;
import com.example.user_project.repository.UserRepository;
import com.example.user_project.serviceimpl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class findAllTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;


    @Test
    public void findAllTest()
    {

        Pageable pageable = PageRequest.of(0,10);

        List<User> userList = Arrays.asList(new User((long)21,"Animesh_Surryawanshi","Animesh","Suryawanshi","Animesh@gmail,com","ANime#@12","Karad","7647562435", LocalDateTime.now(),LocalDateTime.now(),"jkshfyrgbevbgrkelignwtuurj",LocalDateTime.now(),true,false),
                new User((long)21,"Animesh_Surryawanshi","Animesh","Suryawanshi","Animesh@gmail,com","ANime#@12","Karad","7647562435", LocalDateTime.now(),LocalDateTime.now(),"jkshfyrgbevbgrkelignwtuurj",LocalDateTime.now(),true,false)  );

        Page<User> userPage  = new PageImpl(userList);


        Mockito.when(userRepository.findAll(Mockito.any(Pageable.class))).thenReturn(userPage);

        Object result = userService.findAll(pageable);

        Assertions.assertNotNull(result);

        Assertions.assertEquals(userPage,result);

        Mockito.verify(userRepository,Mockito.times(1)).findAll(pageable);

    }




}
