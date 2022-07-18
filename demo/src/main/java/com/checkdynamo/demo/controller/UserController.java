package com.checkdynamo.demo.controller;

import com.checkdynamo.demo.Util.JwtUtil;
import com.checkdynamo.demo.model.JwtRequest;
import com.checkdynamo.demo.model.Usermodel;
import com.checkdynamo.demo.service.Jwtservice;
import com.checkdynamo.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private Jwtservice jwtservice;

    @PostMapping({"/register"})
    private ResponseEntity<?> Register(@RequestBody Usermodel user)throws  Exception{
        return userService.save(user);

    }
    @PostMapping({"/login"})
    private ResponseEntity<?> Login(@RequestBody JwtRequest jwtRequest)  {
return jwtservice.createJwtToken(jwtRequest);

    }


    @PostMapping({"/forgotpassword"})
    public ResponseEntity<?> ForgotPassword (@RequestBody HashMap<String,String> userEmail) throws Exception{
        System.out.println("userrrrr"+userEmail);
        Usermodel user=userService.finduserbyemail(userEmail.get("userEmail"));
        if(user!=null){
            return userService.createforgttoken(user);

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email-address is not registered");
    }

    @PatchMapping({"/changepassword/{id}/{token}"})
    public ResponseEntity<?> ChangePassword(@PathVariable("id") String id,@PathVariable("token") String token, @RequestBody HashMap<String,String> Passwords) throws Exception{

        System.out.println("ffagfggdgsd"+token +"user"+id);

        return userService.verifyforgottoken(id,token,Passwords);
    }



    @GetMapping({"/Buyer"})

    private String Dispaly(){
        return "okkkkk";
    }

    @GetMapping({"/Seller"})

    private String Dispay(){
        return "Seller";
    }

@GetMapping({"/"})
private String Reg(){
    return "OKKKKKKK";
}
}
