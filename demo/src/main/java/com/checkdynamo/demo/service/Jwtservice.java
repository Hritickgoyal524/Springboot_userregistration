package com.checkdynamo.demo.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.checkdynamo.demo.Util.JwtUtil;
import com.checkdynamo.demo.model.JwtRequest;
import com.checkdynamo.demo.model.Usermodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class Jwtservice implements UserDetailsService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private DynamoDBMapper dynamoDBMapper;
    @Autowired
    private JwtUtil jwtUtil;

    public ResponseEntity<?> createJwtToken(JwtRequest jwtRequest){


        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUserEmail(), jwtRequest.getUserPassword()));
        }

        catch (
                BadCredentialsException e){
            System.out.println(e.getMessage());
          return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Credentials do not match");

        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email address is wrong for corresponding password");

        }

        UserDetails userDetails = loadUserByUsername(jwtRequest.getUserEmail());
        String newGeneratedToken = jwtUtil.generateToken(userDetails);


        return ResponseEntity.ok().body(newGeneratedToken);
    }
    @Override
    public UserDetails loadUserByUsername(String s) {
        Usermodel e=new Usermodel();
        e.setUserEmail(s);

        DynamoDBQueryExpression<Usermodel> query=new DynamoDBQueryExpression<Usermodel>().withHashKeyValues(e).withIndexName("userEmail-index").withConsistentRead(false)
                .withLimit(10);

        PaginatedQueryList<Usermodel> emp=dynamoDBMapper.query(Usermodel.class,query);
        if(emp.isEmpty()){
            ResponseEntity.notFound().build();
        }

//        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
//
        List<SimpleGrantedAuthority> authorities=new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(emp.get(0).getRole().getRoletype()));
        System.out.println(emp.get(0).getRole().getRoletype());
        System.out.println(authorities.toArray());
        return new User(emp.get(0).getUserEmail(),emp.get(0).getUserPassword(),authorities);
    }

//
}
