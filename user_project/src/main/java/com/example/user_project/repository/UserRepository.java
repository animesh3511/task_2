package com.example.user_project.repository;

import com.example.user_project.model.User;
import com.example.user_project.projection.projection;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {


    boolean existsByEmail(String email);

    User findByEmail(String email);


    @Query(value ="SELECT * FROM `user` WHERE CONCAT(user_name,' ',email,' ',city) LIKE %:search%",nativeQuery = true)
    Page<User> searchByUserNameEmailCity(String search, Pageable pageable);


    @Query(value = "select user_name as UserName, email as Email, password as Password, city as City from user",nativeQuery = true)
    Page<projection> findByProjection(Pageable pageable);


    User findByToken(String token);

    boolean existsByEmailAndUserIdNotIn(String email, List<Long> userIds);


    Optional<User> findByUserName(String userName);


}
