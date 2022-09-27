package com.checkdynamo.demo.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.checkdynamo.demo.model.Confirmationtoken;
import com.checkdynamo.demo.model.Usermodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Repository
public class UserService {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
@Autowired
private Emailsenderservice emailsenderserice;

//Function for user registration
    public ResponseEntity<?> save(Usermodel user) {


        Usermodel finduser = finduserbyemail(user.getUserEmail());// function for getting user by email id
        if (finduser != null) {
            return ResponseEntity.badRequest().body("Email address is already registered. Please Login!");
        } else {
            user.setUserPassword(getEncodedPassword(user.getUserPassword()));
            user.setConfirm(new Confirmationtoken());

            dynamoDBMapper.save(user); // saving user details in dynamo db
            emailsenderserice.sendMail(user.getUserEmail(),"Thankyou for creating account on StoneAdda! " +
                    "Team StoneAdda ","Greetings from StoneAdda"); // sending email for acknowleding the user


            return ResponseEntity.status(HttpStatus.CREATED).body("Registration Successfully");

        }
    }
// fuction to get encrypted password
    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }
//function used for forget token
    public ResponseEntity<?> createforgttoken(Usermodel user){
        Date date= new Date(System.currentTimeMillis()+1000*30*60); // time duration for token expire
        String token = UUID.randomUUID().toString();  // generating unique ID for token
        String userid=user.getUserId();

        user.getConfirm().setConfirmtoken(userid+token); //saving forget token in user details
        user.getConfirm().setCreatedate(date);
        dynamoDBMapper.save(user);
        String link=userid+"/"+token;
        emailsenderserice.sendMail(user.getUserEmail(),"Forgot password Mail!! StoneAdda","Click on this given link. Link is valid only for 10 minutes!!"+"   http://localhost:8080"+link);
        // sending forgot password link through email


        return ResponseEntity.ok().body(link);

    }
    // fucntion to verify forgot token
    public ResponseEntity<?> verifyforgottoken(String userid, String token, HashMap<String,String> Passwords){
       try {
           Usermodel user = dynamoDBMapper.load(Usermodel.class, userid); // getting user details from dynamo db
           if (user != null) {
               String confirmtoken = user.getConfirm().getConfirmtoken();

               Date tokendate = user.getConfirm().getCreatedate();
               String validatetoken = userid + token;

               if (confirmtoken.equals(validatetoken) && !tokendate.before(new Date())) {
                   String password = Passwords.get("password"); // geting new password from user
                   String confirmpassword = Passwords.get("confirmpassword");
                   if (password.equals(confirmpassword)) {
                       Date date = new Date();

                       String encypass = getEncodedPassword(password); // encoding new password
                       user.setUserPassword(encypass);
                       user.getConfirm().setConfirmtoken("");
                       user.getConfirm().setCreatedate(null);
                       dynamoDBMapper.save(user);
                       return ResponseEntity.ok().body("Updated Successfully! Please login!");
                   } else {
                       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password and confirmpassword do not match");
                   }
               } else {
                   return ResponseEntity.status(HttpStatus.FORBIDDEN).body("NewPassword creation link expired! Please try again with new link");
               }

           } else {
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
           }
       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
       }
    }
    // function of getting user by email address
    public Usermodel finduserbyemail(String email){
        Usermodel e=new Usermodel();
        e.setUserEmail(email);

        DynamoDBQueryExpression<Usermodel> query=new DynamoDBQueryExpression<Usermodel>().withHashKeyValues(e).withIndexName("userEmail-index").withConsistentRead(false)
                .withLimit(10); // query to get user from dynamo db

        PaginatedQueryList<Usermodel> emp=dynamoDBMapper.query(Usermodel.class,query);
        if(!emp.isEmpty()){
            return emp.get(0);
        }
        else{
            return null;
        }
    }
}