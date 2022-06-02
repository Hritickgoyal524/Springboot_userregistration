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
    public ResponseEntity<?> save(Usermodel user) {

        Usermodel finduser = finduserbyemail(user.getUserEmail());
        if (finduser != null) {
            return ResponseEntity.badRequest().body("Email address is already registered. Please Login!");
        } else {
            user.setUserPassword(getEncodedPassword(user.getUserPassword()));
            user.setConfirm(new Confirmationtoken());

            dynamoDBMapper.save(user);
            emailsenderserice.sendMail(user.getUserEmail(),"Thankyou for creating account on StoneAdda! " +
                    "Team StoneAdda ","Greetings from StoneAdda");
            return ResponseEntity.status(HttpStatus.CREATED).body("Registration Successfully");

        }
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public ResponseEntity<?> createforgttoken(Usermodel user){
        Date date= new Date(System.currentTimeMillis()+1000*30*60);
        String token = UUID.randomUUID().toString();
        String userid=user.getUserId();

        System.out.println(date);
        user.getConfirm().setConfirmtoken(userid+token);
        user.getConfirm().setCreatedate(date);
        dynamoDBMapper.save(user);
        String link=userid+"/"+token;
        emailsenderserice.sendMail(user.getUserEmail(),"Forgot password Mail!! StoneAdda","Click on this given link. Link is valid only for 10 minutes!!"+"   http://localhost:7000"+link);
        return ResponseEntity.ok().body(link);

    }
    public ResponseEntity<?> verifyforgottoken(String userid, String token, HashMap<String,String> Passwords){
       try {
           Usermodel user = dynamoDBMapper.load(Usermodel.class, userid);
           if (user != null) {
               String confirmtoken = user.getConfirm().getConfirmtoken();

               Date tokendate = user.getConfirm().getCreatedate();
               String validatetoken = userid + token;

               if (confirmtoken.equals(validatetoken) && !tokendate.before(new Date())) {
                   String password = Passwords.get("password");
                   String confirmpassword = Passwords.get("confirmpassword");
                   if (password.equals(confirmpassword)) {
                       Date date = new Date();
                       System.out.println("date check" + tokendate.before(date));
                       String encypass = getEncodedPassword(password);
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
    public Usermodel finduserbyemail(String email){
        Usermodel e=new Usermodel();
        e.setUserEmail(email);

        DynamoDBQueryExpression<Usermodel> query=new DynamoDBQueryExpression<Usermodel>().withHashKeyValues(e).withIndexName("userEmail-index").withConsistentRead(false)
                .withLimit(10);

        PaginatedQueryList<Usermodel> emp=dynamoDBMapper.query(Usermodel.class,query);
        if(!emp.isEmpty()){
            return emp.get(0);
        }
        else{
            return null;
        }
    }
}