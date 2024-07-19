package com.example.user_project.controller;

import com.example.user_project.model.request.UserRequest;
import com.example.user_project.model.response.CustomEntityResponse;
import com.example.user_project.model.response.EntityResponse;
import com.example.user_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/saveOrUpdate")
    public ResponseEntity<?> saveOrUpdate(@RequestBody UserRequest userRequest)
    {
     try
     {
         return new ResponseEntity(new EntityResponse(userService.saveOrUpdate(userRequest),0), HttpStatus.OK);

     }catch (Exception e)
     {

         return new ResponseEntity(new CustomEntityResponse(e.getMessage(),-1),HttpStatus.BAD_REQUEST);

     }



// saveOrUpdate() method ends here
    }

  @GetMapping("/searchByUserNameEmailCity")
    public ResponseEntity<?> searchByUserNameEmailCity(@RequestParam (defaultValue = "0",required = false)Integer pageNo,
                                                       @RequestParam(defaultValue = "10",required = false) Integer pageSize,
                                                       @RequestParam String search)
  {


   try
   {
       Pageable pageable = PageRequest.of(Optional.ofNullable(pageNo).orElse(0), Optional.ofNullable(pageSize).orElse(0));

       return new ResponseEntity(new EntityResponse(userService.searchByUserNameEmailCity(search,pageable),0),HttpStatus.OK);

   }catch (Exception e)
   {

return new ResponseEntity(new CustomEntityResponse(e.getMessage(),-1),HttpStatus.BAD_REQUEST);
   }


  }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam Long id)
{
    try {

        return new ResponseEntity(new EntityResponse(userService.findById(id),0),HttpStatus.OK);

    }catch (Exception e)
    {


     return new ResponseEntity(new CustomEntityResponse(e.getMessage(),-1),HttpStatus.BAD_REQUEST);

    }


}

   @DeleteMapping("deleteById")
   public ResponseEntity<?> deleteById(Long id)
   {
       try {

           return new ResponseEntity(new EntityResponse(userService.deleteById(id),0),HttpStatus.OK);

       }catch (Exception e)
       {

        return new ResponseEntity(new CustomEntityResponse(e.getMessage(),-1),HttpStatus.BAD_REQUEST);

       }

   }
   @PutMapping("/statusChange")
    public ResponseEntity<?> statusChange(Long id)
   {
       try{

           return new ResponseEntity(new EntityResponse(userService.statusChange(id),0),HttpStatus.OK);

       }catch (Exception e)
       {

        return new ResponseEntity(new CustomEntityResponse(e.getMessage(),-1),HttpStatus.BAD_REQUEST);
       }

   }

   @GetMapping("/getByProjection")
   public ResponseEntity<?> getByProjection(@RequestParam(defaultValue = "0",required = false) Integer pageNo,
                                            @RequestParam(defaultValue = "10",required = false) Integer pageSize)
   {
    Pageable pageable = PageRequest.of(pageNo,pageSize);

       try {

           return new ResponseEntity(new EntityResponse(userService.getByProjection(pageable),0),HttpStatus.OK);

       }catch (Exception e)
       {

        return new ResponseEntity(new CustomEntityResponse(e.getMessage(),-1),HttpStatus.BAD_REQUEST);
       }


   }


   @PostMapping("/changePassword")
   public ResponseEntity<?> changePassword(@RequestParam Long userId,@RequestParam  String oldPassword,
                                            @RequestParam String newPassword)
   {
       try {
           return new ResponseEntity(new EntityResponse(userService.changePassword(userId,oldPassword, newPassword), 0), HttpStatus.OK);
       }catch (Exception e)
       {

          return new ResponseEntity(new CustomEntityResponse(e.getMessage(),-1),HttpStatus.BAD_REQUEST);

       }

  //changePassword() method ends here
   }

   @PostMapping("/forgotPassword")
   public ResponseEntity<?> forgotPassword(@RequestParam String email)
   {
       try{

           return new ResponseEntity(new EntityResponse(userService.forgotPassword(email),0),HttpStatus.OK);
       }catch (Exception e)
       {

         return new ResponseEntity(new CustomEntityResponse(e.getMessage(),-1),HttpStatus.BAD_REQUEST);
       }



    //forgotPassword() method ends here
   }


   @PutMapping("/resetPassword")
  public ResponseEntity<?> resetPassword(@RequestParam String token,@RequestParam String newPassword)
  {
      try{

          return new ResponseEntity(new EntityResponse(userService.resetPassword(token,newPassword),0),HttpStatus.OK);


      }catch (Exception e)
      {

      return new ResponseEntity(new CustomEntityResponse(e.getMessage(),-1),HttpStatus.BAD_REQUEST);

      }


  //resetPassword() method ends here
  }

  @PostMapping("/fileUpload")
  public ResponseEntity<?> fileUpload(@ModelAttribute MultipartFile file)
  {
      try {

        return new ResponseEntity(new EntityResponse(userService.fileUpload(file),0),HttpStatus.OK);


      }catch (Exception e)
      {

       return new ResponseEntity(new CustomEntityResponse(e.getMessage(),-1),HttpStatus.BAD_REQUEST);

      }


  // fileUpload() method ends here
  }

  @GetMapping("/findAll")
  public ResponseEntity<?> findAll(@RequestParam (defaultValue = "0",required = false) Integer pageNo,
                                    @RequestParam (defaultValue = "10",required = false) Integer pageSize)
  {

     Pageable pageable = PageRequest.of(pageNo,pageSize);

    try{

      return new ResponseEntity(new EntityResponse(userService.findAll(pageable),0),HttpStatus.OK);


    }catch (Exception e)
    {

     return new ResponseEntity(new CustomEntityResponse(e.getMessage(),-1),HttpStatus.BAD_REQUEST);

    }



  }






//class ends here

}
