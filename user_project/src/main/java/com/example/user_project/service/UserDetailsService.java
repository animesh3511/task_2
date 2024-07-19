package com.example.user_project.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsService {

    UserDetails loadUserByUserName(String userName) throws Exception;


}
