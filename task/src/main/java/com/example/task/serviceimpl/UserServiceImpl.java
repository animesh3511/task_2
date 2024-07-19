package com.example.task.serviceimpl;

import com.example.task.model.User;
import com.example.task.model.request.UserRequest;
import com.example.task.repository.UserRepository;
import com.example.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public Object saveOrUpdate(UserRequest userRequest) {

        if (userRepository.existsById(userRequest.getUserId()))
        {
            User user = userRepository.findById(userRequest.getUserId()).get();

            user.setUserId(userRequest.getUserId());
            if (isValidUsername(userRequest.getUserName()))
            {
                user.setUserName(userRequest.getUserName());
            }
            else
            {
                return "username is invalid it must contain only alphabets and '_'";

            }
            user.setEmail(userRequest.getEmail());

            if (isPasswordValid(userRequest.getPassword()))
            {
                user.setPassword(userRequest.getPassword());

            }
            else
            {

                return "password must contain an Alphabet,a Digit and a special character and " +
                        " its length must be at least 10";

            }

            user.setCity(userRequest.getCity());
            user.setState(userRequest.getState());
            user.setCountry(userRequest.getCountry());

            if (isZipCodeValid(userRequest.getZipCode())) {

                user.setZipCode(userRequest.getZipCode());
            }
            else
           {

                return "zipcode must be of 6 digit and it must be Alphanumeric";

            }
            userRepository.save(user);

            return  "User Updated";

        }
        else
        {
            User user = new User();

            user.setUserId(userRequest.getUserId());

            if (isValidUsername(userRequest.getUserName()))
            {
                user.setUserName(userRequest.getUserName());
            }
            else
            {
                return "username is invalid it must contain only alphabets and '_' ";

            }
            user.setUserName(userRequest.getUserName());



            if (userRepository.existsByEmail(userRequest.getEmail()))
            {
                return "email already exists";
            }
            user.setEmail(userRequest.getEmail());


            if (isPasswordValid(userRequest.getPassword()))
            {
                user.setPassword(userRequest.getPassword());

            }
            else
            {

                return "password must contain an Alphabet,a Digit and a special character and " +
                        " its length must be at least 10 units";

            }
            user.setCity(userRequest.getCity());
            user.setState(userRequest.getState());
            user.setCountry(userRequest.getCountry());

           if (isZipCodeValid(userRequest.getZipCode())) {

                user.setZipCode(userRequest.getZipCode());
            }
            else
            {

                return "zipcode must be of 6 digit and it must be Alphanumeric";

            }
            user.setZipCode(userRequest.getZipCode());
            userRepository.save(user);
            return  "User saved";

        }



  //saveOrUpdate() method ends here
    }

    @Override
    public Object getAllRecords() {

       return userRepository.findAll();
    }

    @Override
    public Object searchByEmailUserIdUserName(String search, Pageable pageable) {

        Page<User> users;



        if (search != null && !search.isEmpty()) {
            try {
                // Try to parse search as an Long to detect if it's a userId

                Long userId = Long.parseLong(search.trim());


                users = userRepository.searchByEmailUserIdUserName(null, userId, pageable);

                if (users.isEmpty())
                {
                    return "userId does not exist";

                }

            } catch (NumberFormatException e) {
                // If search is not a number, treat it as username or email

                users = userRepository.searchByEmailUserIdUserName(search, null, pageable);

                if (users.isEmpty())
                {
                    if (search.contains("@"))
                    {
                        return "email does not exists";
                    }
                    else
                    {
                        return "userName does not exists";

                    }

                }
            }
        } else {
            // If search is null or empty, fetch all users
            users = userRepository.findAll(pageable);
        }


        return users;

//searchByEmailUserIdUserName() method ends here.
    }

    @Override
    public Object searchByEmailUserNameZipCode(String search, Pageable pageable) {

        Page<User> users = null;

        if (search != null && !search.isEmpty())
        {

             users = userRepository.searchByEmailUserNameZipCode(search,pageable);

             if (users.isEmpty())
             {
                 if (search.contains("@"))
                 {
                     return "email does not exists";

                 }
                 else if (isZipCodeValid(search))
                 {

                     List<User> list = userRepository.findAll();


                     Long count = list.stream().filter((s)-> s.getZipCode().equals(search)) .count();

                     if (count ==0 )
                     {
                         return "zipcode does not exist";

                     }
//                     for (User user : list)
//                    {
//
//                         if (user.getZipCode() == search)
//                         {
//                            count++;
//
//                         }
//
//                     }
//                     if (count ==0)
//                     {
//                         return "zipcode does not exists";
//                     }

                 }
                 else
                 {
                     return "username does not exists";

                 }


             }


        }
        else {
            // If search is null or empty, fetch all users
            users = userRepository.findAll(pageable);
        }


//        if (users.isEmpty())
//        {
//            return "data not available";
//
//        }
        return users;
// searchByEmailUserNameZipCode() method ends here
    }


    private boolean isValidUsername(String username) {
        // Regular expression to match alphanumeric characters and underscores only
        String regex = "^[a-zA-Z0-9_]+$";
        return username.matches(regex);
    }


    public boolean isPasswordValid( String password) {
        // Password must be at least 10 characters long
        if (password == null || password.length() < 10) {
            return false;
        }

        // Password must contain at least one digit
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                hasDigit = true;
                break;
            }
        }
        if (!hasDigit) {
            return false;
        }

        // Password must contain at least one letter (a-z or A-Z)
        boolean hasLetter = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
                break;
            }
        }
        //here !hasLetter he ek negation ahe.so, jar 'hasLetter' jar true asel tr '!hasLetter' false asty
        //so below condition true vhychi asel tr '!hasLetter' la 'true' asava lagel trach if chi statement execute hoel
        //so jar '!hasLetter' jar true al and compiler if chya aat gela tr false return krel yachach earth 'hasLetter'
        //false asel bcoz '!hasLetter' he negation ahe. aapn hi condition eth lavli bcoz jeva loop break hoel or
        //loop sampurna execute houn pn tyala password chya character array mdhe letter sapadnar nahi tr don conditions arise
        //hou shaktat 1) jar letter sapadla tar 'hasLetter' true hoel n loop break hoel and compiler pudhch execution
        //continue krel so at the end of this method aapn 'return true' kelel ahe so hya case mdhe jeva letter sapadla tr
        //obviously 'true' return karych ahe but for other condition
        //2) jar password chya char array mdhe letter nahi sapadla tr aplyala 'false' return karav lagel but at the end aapn
        //return 'true' kelel ahe so compiler always 'true' return karel so,tasa na vhawa and hya case madhe false return vhava
        //mhnun aapn hii '!hasLetter' negation chi condition takli ahe
        if (!hasLetter) {
            return false;
        }

        // Password must contain at least one special character from [@#$%^&+=!]
        boolean hasSpecialChar = false;
        String specialChars = "@#$%^&+=!";
        for (char c : password.toCharArray()) {
            if (specialChars.contains(String.valueOf(c))) {
                hasSpecialChar = true;
                break;
            }
        }
        if (!hasSpecialChar) {
            return false;
        }

        return true;
        //isPasswordValid() method ends here
    }


    public boolean isZipCodeValid(String zipCode) {
        // Regular expression to match exactly six alphanumeric characters
        String regex = "^[a-zA-Z0-9]{6}$";
        return zipCode != null && zipCode.matches(regex);
    }




}
