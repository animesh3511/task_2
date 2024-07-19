package com.example.user_project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "city")
    private String city;

    @Column(name = "email")
   private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "contact")
    private String contact;

    //marks the property as creation timestamp for owning entity.the value that is set to this
    //property is set exactly once when saving the owning entity.and it cannot be updated.the
    //value will be current timestamp of VM at the time of saving entity
    @CreationTimestamp
    @Column(name = "student_created_at",updatable = false)
    private LocalDateTime userCreatedAt;

    //this annotation marks the property field of owning entity as update timestamp.so whenever
    //the entity is updated that current timestamp of VM will be set as value to this property
    @UpdateTimestamp
    @Column(name = "student_updated_at")
    private LocalDateTime userUpdatedAt;

    //this is token used for forgot password api
    private String token;

    @Column(name = "TokenTimeStamp")
    private LocalDateTime tokenCreationTime;


    //both of following fields have default values as 1,0 respectively in mysql db schema.so,those
    //are assigned according to those values
    private Boolean isActive;

    private Boolean isDeleted;


    //this is the constructor which accepts follwing strings. i have created this for purpose of Junit testing method
    //of getByProjection method
    public User(String userName, String email, String password, String city) {

    }
}
