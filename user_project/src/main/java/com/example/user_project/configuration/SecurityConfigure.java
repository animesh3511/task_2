package com.example.user_project.configuration;

import com.example.user_project.filter.JwtTokenFilter;
import com.example.user_project.model.User;
import com.example.user_project.repository.UserRepository;
import com.example.user_project.serviceimpl.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;

//indicates that this class provides spring configuration
@Configuration
//enables spring security's web security support
@EnableWebSecurity
//here, 'WebSecurityConfigurerAdapter' is base class for spring security's web security.we override two 'configure'
//methods of this class.
public class SecurityConfigure extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

//this is overriden method of 'WebSecurityConfigurer' class used to configure 'authentication manager'
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        //here,we implement custom 'usersetailsservice' interface to retrieve user object using userRepository
    //here as 'UserDetailsService' interface have only single method we treted it as functional interface
   auth.userDetailsService(userName -> {
     //here 'User' object is being returned dorresponding to 'userName'
     User user = userRepository.findByUserName(userName)
                     .orElseThrow(()-> new UsernameNotFoundException("userName not found with " +userName));

     //here we send 'user' object to 'UserDetailsImpl' classes 'build' method which returns 'UserDetails' objects
        UserDetails userDetails = UserDetailsImpl.build(user);

        return userDetails;
       //here we call passwordEncoder method with arg call to 'passworEncoder()' which is declared below it is used
       //it is used to match password that we send through postman with bcrypted passwords in db
   }).passwordEncoder(passwordEncoder());



    }

   //creates a bean to be manage by spring container
   @Bean
   public PasswordEncoder passwordEncoder()
   {

    return new BCryptPasswordEncoder();
   }

//here, we override 'configure' method of 'WebSecurityConfigurerAdapter' class to configure HttpSecurity object to
//provide web security support to spring security
    @Override
    public void configure(HttpSecurity http) throws Exception
    {
      //here, Enabled CORS() and Disabled CSRF()
      http = http.cors().and().csrf().disable();

      //here we set session to stateless
      http = http.sessionManagement()
                 .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                 .and();

     //creating exception handler for unauthorized requests
        http = http
                .exceptionHandling()
                .authenticationEntryPoint((request, response, ex) -> {

                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,ex.getMessage());

                })

                .and();

    // here, setting permissions on endpoints

             http.authorizeRequests()
                 //here are public requests
              .antMatchers(HttpMethod.POST,"/api/saveOrUpdate").permitAll()
              //.antMatchers(HttpMethod.GET,"/api/**").permitAll()
              //.antMatchers(HttpMethod.PUT,"/api/**").permitAll()
              .antMatchers("/api/login/**").permitAll()
                     .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll() // Allow access to Swagger UI


          //any requests other than above requests are private requests and need authentication
              .anyRequest().authenticated();


           //adding a JWT Token Filter
           http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);


    }


//used by spring security if cors is enabled. it allows request from any origin,headers,methods and does not give
    //error of 'CORS POLICY'
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }



    @Override
    @Bean
    //here we override this method to expose 'Authrntication Manager' as spring bean
    //it is required to use 'AuthenticationManager' in other parts of this project like in 'AuthApi controller'
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
