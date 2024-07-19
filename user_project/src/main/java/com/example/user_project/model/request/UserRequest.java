package com.example.user_project.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequest {

 private Long userId;

 private String userName;

 private String firstName;

private String lastName;

private String city;

private String email;

private String password;

private String contact;


}
