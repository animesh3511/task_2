package com.example.user_project;

import com.example.user_project.model.User;
import com.example.user_project.projection.projection;
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

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class getByProjectionTest {


    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

   private Pageable pageable;

   private List<User> userList;

    private  Page<projection> userPage;

    @BeforeEach
    void setUp()
    {


         pageable = PageRequest.of(0,10);


         userList = Arrays.asList(new User("Animesh_Suryawanshi","Animeah@gmail.com","ANimesh@1234","Karad"));

         userPage = new PageImpl(userList);

    }


    @Test
    public void getByProjectionTest()
    {

        Mockito.when(userRepository.findByProjection(pageable)).thenReturn(userPage);

        Object result = userService.getByProjection(pageable);

        Assertions.assertEquals(userPage,result);

        Mockito.verify(userRepository,Mockito.times(1)).findByProjection(pageable);


    }



}




/*


import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
        import org.junit.jupiter.api.extension.ExtendWith;
        import org.mockito.InjectMocks;
        import org.mockito.Mock;
        import org.mockito.junit.jupiter.MockitoExtension;
        import org.springframework.data.domain.Page;
        import org.springframework.data.domain.PageImpl;
        import org.springframework.data.domain.PageRequest;
        import org.springframework.data.domain.Pageable;

        import static org.junit.jupiter.api.Assertions.assertEquals;
        import static org.mockito.Mockito.when;

        import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private Pageable pageable;
    private Page<Object> page;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
        page = new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    @Test
    void testGetByProjection() {
        when(userRepository.findByProjection(pageable)).thenReturn(page);

        Page<Object> result = (Page<Object>) userService.getByProjection(pageable);

        assertEquals(page, result);
    }
}
*/
