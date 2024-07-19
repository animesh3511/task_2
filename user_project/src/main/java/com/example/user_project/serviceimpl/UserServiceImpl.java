package com.example.user_project.serviceimpl;

import com.example.user_project.model.User;
import com.example.user_project.model.request.UserRequest;
import com.example.user_project.repository.UserRepository;
import com.example.user_project.service.UserService;
import com.lowagie.text.DocumentException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
//import org.w3c.dom.stylesheets.LinkStyle;
import org.xhtmlrenderer.pdf.ITextRenderer;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TemplateEngine templateEngine;

    //this is for UUID token used to reset password
    private static final long EXPIRE_TIME = 30;

    //this string is used as argument for method sendEmailWithAttachment() which i have defined below in this class
    //and calling inside update and save method
    String attachment = "E:\\Animesh\\up.txt";




    @Override
    public Object saveOrUpdate(UserRequest userRequest) throws MessagingException, DocumentException {


   //updating user
   if (userRepository.existsById(userRequest.getUserId()))
   {

       User user  = userRepository.findById(userRequest.getUserId()).get();


       user.setUserId(userRequest.getUserId());
       if (isUserNameValid(userRequest.getUserName())) {
           user.setUserName(userRequest.getUserName());
       }
       else
       {
           return "username must contain only alphabets and '_' ";
       }
       user.setFirstName(userRequest.getFirstName());
       user.setLastName(userRequest.getLastName());

       List<Long> userIds = new ArrayList<>();
       userIds.add(userRequest.getUserId());
       if (userRepository.existsByEmailAndUserIdNotIn(userRequest.getEmail(),userIds))
       {
          return "email already exists";
       }
       else {
           user.setEmail(userRequest.getEmail());
       }
       if (isPasswordValid(userRequest.getPassword())) {

           String hashedPassword = hashPassword(userRequest.getPassword());
           user.setPassword(hashedPassword);


          // user.setPassword(userRequest.getPassword());
       }
       else
       {

        return "password must be 5 to 15 units long and contain a digit,at least one lower case alphabet and a special character";
       }
       user.setCity(userRequest.getCity());
       if (isContactValid(userRequest.getContact())) {
           user.setContact(userRequest.getContact());
       }
       else
       {

        return "contact must be of 10 digits";
       }
       user.setIsActive(true);
       user.setIsDeleted(false);
       userRepository.save(user);


       //calling sendEmail() method which is defined below in this class

     //  sendEmail("animesh3511@gmail.com","ritujagawade9799@gmail.com","regarding updating your info","your info is updated succesfully");

       //calling sendEmailWithattachment() method here which is defined below in this class

      // sendEmailWithAttachment("animesh3511@gmail.com","ritujagawade9799@gmail.com","regarding updating your info","your info is updated succesfully",attachment);



       //creating Context object here which i have to send as argument to method sendEmailWithTemplate() which i have
       //defined below in this class

       //initializing templateName here which i will send as arg to sendEmailWithTemplate() method
       String templateName = "emailTemplate";

       //he context object api chya aat initialize kel trach hyachya method available hotat. globally kel tr hot nahit
       Context c = new Context();
       c.setVariable("name",userRequest.getFirstName());
       c.setVariable("message","this is html template");

       //calling sendEmailWithTemplate() method here
       //sendEmailWithTemplate("animesh3511@gmail.com","ritujagawade9799@gmail.com","regarding saving your info",templateName,c);

       String otp = generateOtp();
       System.out.println(otp);

       return "user updated";
   }

   //saving user starts here
   else
   {
       User user = new User();

       user.setUserId(userRequest.getUserId());
       if (isUserNameValid(userRequest.getUserName())) {
           user.setUserName(userRequest.getUserName());
       }
       else
       {
           return "username must contain only alphabets and '_' ";
       }
       user.setFirstName(userRequest.getFirstName());
       user.setLastName(userRequest.getLastName());
       if (userRepository.existsByEmail(userRequest.getEmail()))
       {
           return "email already exists";
       }
       else {
           user.setEmail(userRequest.getEmail());
       }
       if (isPasswordValid(userRequest.getPassword())) {

           String hashPassword = hashPassword(userRequest.getPassword());
           user.setPassword(hashPassword);

         //  user.setPassword(userRequest.getPassword());
       }
       else
       {

           return "password must be 5 to 15 units long and contain a digit,at least one lower case alphabet and a special character";
       }
       user.setCity(userRequest.getCity());
       if (isContactValid(userRequest.getContact())) {
           user.setContact(userRequest.getContact());
       }
       else
       {

           return "contact must be of 10 digits";
       }

       user.setIsActive(true);
       user.setIsDeleted(false);
       userRepository.save(user);

       //calling sendEmail() method here which is defined in this class below

       this.sendEmail("animesh3511@gmail.com","ritujagawade9799@gmail.com","regarding saving info in database","your information is saved succesfully");

       //calling sendEmailWithattachment() method here which is defined below in this class

      // sendEmailWithAttachment("animesh3511@gmail.com","ritujagawade9799@gmail.com","regarding updating your info","your info is updated succesfully",attachment);

       //creating Context object here which i have to send as argument to method sendEmailWithTemplate() which i have
       //defined below in this class

       //initializing templateName here which i will send as arg to sendEmailWithTemplate() method
     //  String templateName = "emailTemplate";

       //he context object api chya aat initialize kel trach hyachya method available hotat. globally kel tr hot nahit
    //   Context c = new Context();
      // c.setVariable("name",userRequest.getFirstName());
   //    c.setVariable("message","this is html template");

       //calling sendEmailWithTemplate() method here
      // sendEmailWithTemplate("animesh3511@gmail.com","ritujagawade9799@gmail.com","regarding saving your info",templateName,c);

       return "user saved";

   }


   //saveOrUpdate() method ends here
    }

    @Override
    public Object searchByUserNameEmailCity(String search, Pageable pageable) {


       Page <User> user = (search != null && !search.isEmpty()) ? userRepository.searchByUserNameEmailCity(search, pageable) : userRepository.findAll(pageable);
      // if (search != null && !search.isEmpty()) {

          // user = userRepository.searchByUserNameEmailCity(search, pageable);

           if (user.isEmpty())
           {
               if (search.contains("@"))
               {

                 return "email does not exists";
               }
               else if (isUserNameValid(search) && search.contains("_"))
               {

               return "username does not exist";

               }
               else
               {

                 return "city does not exists";
               }

           }

      // }
      // else
      // {
      //    user = userRepository.findAll(pageable);

      // }
  
      return user;
       //searchByUserNameEmailCity() method ends here
    }

    @Override
    public Object findById(Long id) throws Exception {

     return  userRepository.findById(id).orElseThrow(()->new Exception("user not found"));

    //findById() method ends here
    }

    @Override
    public Object deleteById(Long id) {


        if (userRepository.existsById(id))
        {

           userRepository.delete(userRepository.findById(id).get());

            return "user deleted succesfully";
        }
        else
        {

           return "user not found";
        }


     //deleteById() method ends here
    }
    @Override
    public Object statusChange(Long id) throws Exception {

       return userRepository.findById(id)
               .map(user -> { user.setIsActive(!user.getIsActive());
                              user.setIsDeleted(!user.getIsDeleted());
                              userRepository.save(user);
                             return user.getIsActive()?"user is active":"user is not active";
               })
               .orElseThrow(()->  new Exception("user not found"));


/*
        if (userRepository.existsById(id))
        {
           User user = userRepository.findById(id).get();

           if (user.getIsActive())
           {

               user.setIsActive(false);
               userRepository.save(user);
               return "user is not active";

           }
           else
           {
               user.setIsActive(true);
               userRepository.save(user);
               return "user is active";

           }

        }
        else
        {

          throw new Exception("user not found");
        }*/

//statusChange() method ends here.
    }

    @Override
    public Object getByProjection(Pageable pageable) {


     return userRepository.findByProjection(pageable);

  //getByProjection() method ends here
    }

    @Override
    public Object changePassword(Long userId,String oldPassword, String newPassword) {

    if (userId != 0)
    {

     User user = userRepository.findById(userId).get();
     String dbPassword = user.getPassword();


     if (dbPassword.equalsIgnoreCase(oldPassword))
     {
         String hashedPassword = hashPassword(newPassword);
         user.setPassword(hashedPassword);
         userRepository.save(user);
         return "user is updated with new password";

     }
     else
     {

       return "oldPassword is incorrect";
     }

    }
    else
    {

     return "userId is incorrect";
    }



    //changePassword() method ends here
    }

    @Override
    public Object forgotPassword(String email) {



       Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));

      if (!userOptional.isPresent())
      {

        return "Invalid email id";

      }

      User user = userOptional.get();

      user.setToken(generateToken());
      user.setTokenCreationTime(LocalDateTime.now());

      userRepository.save(user);
      return user.getToken();

    // forgotPassword() method ends here
    }

    @Override
    public Object resetPassword(String token, String newPassword) {

       Optional<User> userOptional = Optional.ofNullable(userRepository.findByToken(token));

       if (!userOptional.isPresent())
       {

           return "Invalid Token";

       }



       LocalDateTime tokenCreatedTimeDate = userOptional.get().getTokenCreationTime();

       if (isTokenExpired(tokenCreatedTimeDate))
       {

          return "Token Expired";

       }

       User user = userOptional.get();
       String hashPassword= hashPassword(newPassword);
       user.setPassword(hashPassword);
       user.setToken(null);
       user.setTokenCreationTime(null);

       userRepository.save(user);

       return "Your password is updated succesfully";


   //resetPassword() method ends here
    }

    @Override
    public Object fileUpload(MultipartFile file) throws IOException {

   String originalFileName = file.getOriginalFilename();

  if (!file.isEmpty() && file != null)
  {

    // String storagePath = "E:\\Animesh\\myWorkspace";

      String storagePath = "E:\\Ruchi";

      Path path = Paths.get(storagePath,originalFileName);

      Files.write(path,file.getBytes());

  }

  return originalFileName;


    //fileUpload() ends here
    }

    @Override
    public Object findAll(Pageable pageable) {


       return userRepository.findAll(pageable);

    }


    @Value("${spring.mail.username}")
    private String fromEmail;
    public String getFromEmail()
    {return fromEmail;}

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to,String cc,String subject,String body)
    {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setCc(cc);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);
        javaMailSender.send(simpleMailMessage);


//sendEmail() method ends here
    }


    public void  sendEmailWithAttachment(String to,String cc,String subject,String body,String attachment) throws MessagingException {

       MimeMessage mimeMessage  = javaMailSender.createMimeMessage();
       MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);

       mimeMessageHelper.setFrom(fromEmail);
       mimeMessageHelper.setTo(to);
       mimeMessageHelper.setCc(cc);
       mimeMessageHelper.setSubject(subject);
       mimeMessageHelper.setText(body);
       FileSystemResource fileSystemResource = new FileSystemResource(new File(attachment));

       mimeMessageHelper.addAttachment(fileSystemResource.getFilename(),fileSystemResource);
       javaMailSender.send(mimeMessage);


    //sendEmailWithAttachment() method ends here
    }


    public void sendEmailWithTemplate(String to, String cc, String subject,String templateName, Context context) throws MessagingException, DocumentException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper= new MimeMessageHelper(mimeMessage,true,"utf-8");

        try {

            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setCc(cc);
            mimeMessageHelper.setSubject(subject);

              //here, templateEngine.process processes email template which is named as "emailTemplate"
             //'emailTemplate.html' hya html template mdhe je ki templates hya package mdhe define kely
            // kahi placeholders ahet. in this case "name" & "message" hyana value Context chya object mdhun detat
            String htmlContent = templateEngine.process(templateName, context);
            mimeMessageHelper.setText(htmlContent, true);

            //ithun pudh PDF ch logic ahe

            //ha ek java class ahe. jo kahi PDF content create hoel tyacha binary data hold krnyasathi
            //hya class chya object cha upyog hoto.tyasathi hya class cha object memory mdhe ek buffer
            //provide krto
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            //this class is provided by itext library
            ITextRenderer iTextRenderer = new ITextRenderer();


            //this is method of ITextRenderer class. this takes String argument which contains HTML data
            //je vrti aapn .process method n process kely. & hi method hi String arg as a 'document' set
            //krte je ki PDF mdhe render krtat
            iTextRenderer.setDocumentFromString(htmlContent);

            //this method calculates the layout of the document. this step in neccessary before creating
            //PDF to ensure that all elements are properly positioned and sized
            iTextRenderer.layout();


            //this method generates PDF from document.you can see 'byteArrayOutputSream' is the arg here
            //bcoz ji PDF generate hoel tyache content hy 'byteArrayOutputStream' mdhe write kele jatil
            //bcoz ha 'byteArrayOutputStream' memory mdhe buffer provide krto tyat he PDF che write kele
            //jatat
            iTextRenderer.createPDF(byteArrayOutputStream);


            mimeMessageHelper.setText("PDF", true);

            //'byteArrayOutputStream.toByteArray()' hi method o/p stram che contents 'byte array' mdhe convert
            //krte. so that aapn te 'addAttachment()' la arg mhnun pass kru shaku. ha 'byte array' generated
            //PDF cha binary data represent krto
            mimeMessageHelper.addAttachment("Document.pdf", new ByteArrayResource(byteArrayOutputStream.toByteArray()));

            javaMailSender.send(mimeMessage);

        }catch (Exception e)
        {
            e.printStackTrace();

        }

      //sendEmailWithTemplate(); method ends here
    }


    public String generateOtp()
    {
        Random random = new Random();
        int otp = random.nextInt(999999);

        return String.valueOf(otp);

    // generateOtp() method ends here
    }

    public String hashPassword(String plainPassword)
    {

     return BCrypt.hashpw(plainPassword,BCrypt.gensalt());

    }



    // condition for userName => it must contain 'a-zA-Z_' only this characters

    public boolean isUserNameValid(String username)
    {
        String regex = "^[a-zA-Z_ ]+$";
        return username.matches(regex);

    //method ends here
    }

    //conditions for password => at least 10 units must contain a digit ,an alphabet upper or lower,
    //a special character.min 5 to max 10 chars
    public boolean isPasswordValid(String password)
    {
//       String regex = "^[a-zA-Z0-9$#@!^%*=+]{5,15}$";
//
//        return password.matches(regex);

        if (password == null || password.length()<5 ||password.length()>15)
        {
          return  false;
        }



        Boolean hasAlphabets = false;

        for (char ch='a';ch<='z';ch++)
        {
            for (int i=0;i<password.toCharArray().length;i++)
            {

                if (ch==password.toCharArray()[i])
                {

                  hasAlphabets = true;
                  break;

                }

            }


        }
        if(!hasAlphabets)
        {
            return false;
        }


        Boolean hasDigit = false;


            for (char c: password.toCharArray())
            {
                if (Character.isDigit(c)) {


                        hasDigit=true;
                        break;


                }


            }
           /* for (int j=0;j<password.toCharArray().length;j++)
            {


                if (i==password.toCharArray()[j])
                {

                    hasDigit=true;
                    break;

                }


            }*/




        if (!hasDigit)
        {

            return false;

        }


      /*  String regex = "^[a-zA-Z0-9!@#$%^&*+=?]+$";

       boolean value= password.matches(regex);


        if (!value)
        {

            return false;
        }*/

        Boolean hasSpecialCharacter = false;
        String special = "!@#$%^&*()_+?";
        for (char c: special.toCharArray())
        {

            boolean s = password.contains(String.valueOf(c));
            if (s)
            {

                hasSpecialCharacter = true;
                break;

            }
          //  System.out.println(s);
        }

        if (!hasSpecialCharacter)
        {
            return false;
        }

        return true;
      //isPasswordValid() method ends here
    }


    //condition for contact => it must be of 10 digits

    public boolean isContactValid(String contact)
    {
      String regex = "^[0-9]{10}$";

      return contact.matches(regex);
    }


    public String generateToken()
    {
        StringBuilder token = new StringBuilder();

        String generatedToken = token.append(UUID.randomUUID().toString())
                                     .append(UUID.randomUUID().toString()).toString();

        System.out.println("Generated Token :"+generatedToken);

        return generatedToken;

     //generateToken() method ends here
    }


    private boolean isTokenExpired(LocalDateTime tokenCreatedDateTime) {


        LocalDateTime now = LocalDateTime.now();

        Duration difference = Duration.between(tokenCreatedDateTime,now);

        return difference.toMinutes() >= EXPIRE_TIME;


     //isTokenExpired() method ends here
    }

  //  class ends here
}
