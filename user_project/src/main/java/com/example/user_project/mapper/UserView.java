package com.example.user_project.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserView {


   private Long userId;
   private String userName;
   private String firstName;
   private String lastName;
   private String city;
   private String email;
   private String password;
   private String contact;
   private LocalDateTime userCreatedAt;
   private LocalDateTime userUpdatedAt;
   private Boolean isActive;
   private Boolean isDeleted;






}
