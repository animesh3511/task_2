package com.example.task.repository;

import com.example.task.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {


    boolean existsByEmail(String email);

   @Query(value = "SELECT  * FROM `user` WHERE CONCAT(user_name,' ',email,' ',zipcode) LIKE %:search%",nativeQuery = true)
   Page<User> searchByEmailUserNameZipCode(String search,Pageable pageable);




    // both of below queries gives expected output when we send userId.The following query contains 'count query'
    //which returns number of records fetched by main @Query
//    @Query(value = "SELECT * FROM user u " +
//            "WHERE (:search IS NULL OR u.user_name LIKE %:search% OR u.email LIKE %:search%) " +
//            "AND (:userId IS NULL OR u.user_id = :userId)",
//            countQuery = "SELECT count(*) FROM user u " +
//                    "WHERE (:search IS NULL OR u.user_name LIKE %:search% OR u.email LIKE %:search%) " +
//                    "AND (:userId IS NULL OR u.user_id = :userId)",
//            nativeQuery = true)

   /* @Query(value = "SELECT * FROM user u " +
            "WHERE (:search IS NULL OR u.user_name LIKE %:search% OR u.email LIKE %:search%) " +
            "AND (:userId IS NULL OR u.user_id = :userId)",nativeQuery = true)*/

    //jar 'LIKE' operator vaparlas tar samaj '@example.com' hi arg postman mdhun 'requestparam' mhnun pathavlis
    //tar saglya values jya '@example.com' contain kartat tya return hotil. so, jar tula exact value havi asel
    // and '@example.com' taklyavr 'email does not exist' asa message yava asa asel tr '=' operator vapar
    //tyamule exact match milel

    @Query(value = "SELECT * FROM user u " +
            "WHERE (:search IS NULL OR u.user_name = :search OR u.email = :search) " +
            "AND (:userId IS NULL OR u.user_id = :userId)",
            nativeQuery = true)
    Page<User> searchByEmailUserIdUserName(
            @Param("search") String search,
            @Param("userId") Long userId,
            Pageable pageable);


    @Query(value = "SELECT * FROM `user`",nativeQuery = true)
    Page<User> getAll(Pageable pageable);


}
