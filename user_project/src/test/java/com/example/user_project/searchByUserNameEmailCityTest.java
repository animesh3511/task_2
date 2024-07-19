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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class searchByUserNameEmailCityTest {


    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    private Pageable pageable;

    private List<User> userList;

    private Page<User> userPage;

    @BeforeEach
    void setUp()
    {

         pageable = PageRequest.of(0,10);


         userList = Arrays.asList(new User((long)21,"Animesh_Surryawanshi","Animesh","Suryawanshi","Animesh@gmail,com","ANime#@12","Karad","7647562435", LocalDateTime.now(),LocalDateTime.now(),"jkshfyrgbevbgrkelignwtuurj",LocalDateTime.now(),true,false),
                new User((long)21,"Animesh_Surryawanshi","Animesh","Suryawanshi","Animesh@gmail,com","ANime#@12","Karad","7647562435", LocalDateTime.now(),LocalDateTime.now(),"jkshfyrgbevbgrkelignwtuurj",LocalDateTime.now(),true,false)  );


         userPage = new PageImpl<>(userList);


    }

   @Test
    public void searchByUserNameEmailCityTest()
    {


        Mockito.when(userRepository.searchByUserNameEmailCity(Mockito.anyString(),Mockito.any(Pageable.class))).thenReturn(userPage);


       Object result = userService.searchByUserNameEmailCity("Animesh_Suryawanshi",pageable);


        Assertions.assertEquals(userPage,result);

        Mockito.verify(userRepository,Mockito.times(1)).searchByUserNameEmailCity("Animesh_Suryawanshi",pageable);


    }


    @Test
    public void searchByUserNameEmailCityFailTest()
    {

        Mockito.when(userRepository.findAll(pageable)).thenReturn(userPage);

        Object result = userService.searchByUserNameEmailCity(null,pageable);

        Assertions.assertNotNull(userPage);

        Assertions.assertEquals(userPage,result);

        Mockito.verify(userRepository,Mockito.times(1)).findAll(pageable);




    }




}
