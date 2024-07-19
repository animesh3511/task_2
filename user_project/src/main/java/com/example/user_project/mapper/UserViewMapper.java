package com.example.user_project.mapper;

import com.example.user_project.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserViewMapper {


    public UserView toUserView(User user)
    {

   UserView userView = new UserView();

   userView.setUserId(user.getUserId());

   userView.setUserName(user.getUserName());

   userView.setFirstName(user.getFirstName());

   userView.setLastName(user.getLastName());

   userView.setCity(user.getCity());

   userView.setContact(user.getContact());

   userView.setEmail(user.getEmail());

   userView.setPassword(user.getPassword());

   userView.setIsActive(user.getIsActive());

   userView.setIsDeleted(user.getIsDeleted());

   userView.setUserCreatedAt(user.getUserCreatedAt());

   userView.setUserUpdatedAt(user.getUserUpdatedAt());

   return userView;

    }

}
