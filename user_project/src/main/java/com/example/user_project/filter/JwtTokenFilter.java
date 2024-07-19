package com.example.user_project.filter;

import com.example.user_project.model.User;
import com.example.user_project.repository.UserRepository;
import com.example.user_project.serviceimpl.UserDetailsImpl;
import com.example.user_project.utils.JwtTokenUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

//this class extends 'OncePerRequestFilter' class which ensures the methods inside this class are executed once per
//request cycle
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    //here,we retrieve authorization header from http request which passes through JwtTokenFilter
    final String header =request.getHeader(HttpHeaders.AUTHORIZATION);

    //here, if conditions inside 'if()' are true then it continues with other filters of filter chain without
        // attempting to process jwt token and skipping 'JwtTokenFilter'.'return' means it stops the execution
    //of current filter and continues with execution of other filters in filter chain
    if (StringUtils.isEmpty(header) || !header.startsWith("Bearer "))
    {

     filterChain.doFilter(request,response);
     return;

    }

 //here, header contains value such as 'Bearer <token>'.header's value we have extracted above from requests header
 //here we split this header into array of string using delimiter " " and then we access element at index [1] of this
 //array and trim any leading and trailing whitespaces and in such way we obtain only the value of token
   final String token =header.split(" ")[1].trim();

    //here, we pass that token to 'validate' method which we have defined in 'JwtTokenUtils' class. It returns us
    //boolean true or false by checking token is valid or not
      if (!jwtTokenUtil.validate(token))
      {
   //if token is not valid then we continue with other filters of filter chain
       filterChain.doFilter(request,response);
       //we stop the execution of current filter
       return;

      }

      String userName = jwtTokenUtil.getUsername(token);

     User user  = userRepository.findByUserName(userName).orElseThrow(()->  new Exception("User not found"));

       UserDetails userDetails = UserDetailsImpl.build(user);

//here,'UsernamePasswordAuthenticationToken' is a class provided by spring security that represents 'authentication
    //token' for username-password authentication mechanism
 //this token is constructed with three parameters.'userDetails' this represents the authenticated principal,in this case
 //it is instance of 'UserDetails' interface obtained from 'UserDetailsImpl.build(user)' call above.
 //second parameter is 'credentials' which is 'null' bcoz in JWT authentication token itself serve as credentials.
 //third parameter is authorities granted to the authenticated principal here, if userDetails are null then empty list
 // is used and if it is not null then 'grantedAuthorities' list is provided
        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails == null ?
                        Arrays.asList() : userDetails.getAuthorities()

        );


  //here, we set additional details about authentication
  //we create instance of 'WebAuthenticationDetailsSource' it is class in spring security which is responsible
  // for creating 'WebAuthenticationDetails' objects.'WebAuthenticationDetails' contains details about the
        // current HTTP request such as remote address, session ID, and the request URI.
   //the method '.buildDetails(request)' builds the 'WebAuthenticationDetails' object based on provided 'request'
    //'authentication.setDetails()' this method of 'UsernamePasswordAuthenticationToken' sets additional details
    //for 'authentication' token
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

  //'SecurityContextHolder.getContext()' retrieves 'securityContext' of current thread which contains details such as
  // authenticated principal and their authenticated authorities
 //'setAuthentication(authentication)' this sets provided authentication object as the authenticated principal in current
  //security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);


        System.out.println("Authorities: " + authentication);


    }


}
