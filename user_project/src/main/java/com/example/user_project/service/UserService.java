package com.example.user_project.service;

import com.example.user_project.model.request.UserRequest;
import com.lowagie.text.DocumentException;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;

public interface UserService {


    Object saveOrUpdate(UserRequest userRequest) throws MessagingException, DocumentException;

    Object searchByUserNameEmailCity(String search, Pageable pageable);

    Object findById(Long id) throws Exception;

    Object deleteById(Long id);

    Object statusChange(Long id) throws Exception;

    Object getByProjection(Pageable pageable);

    Object changePassword(Long userId,String oldPassword, String newPassword);

    Object forgotPassword(String email);

    Object resetPassword(String token, String newPassword);

    Object fileUpload(MultipartFile file) throws IOException;

    Object findAll(Pageable pageable);
}
