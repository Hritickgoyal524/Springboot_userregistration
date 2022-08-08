package com.checkdynamo.demo.config;

import com.checkdynamo.demo.controller.Oauth2successhandler;
import com.checkdynamo.demo.service.Customouth2userservice;
import com.checkdynamo.demo.service.Jwtservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity

public class Secuirtyconfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private Jwtservice jwtser;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private Customouth2userservice customouth2userservice;
    @Autowired
    private Oauth2successhandler oauth2successhandler;
@Autowired
private JwtRequestFilter jwtRequestFilter;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtser);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
http.cors();
        http.csrf().disable()
                .authorizeRequests().antMatchers("/register", "/login","/forgotpassword","/changepassword/**/**","/otpGenerate","/otpValidate","/","/finbyname/*").permitAll().
        antMatchers("/Seller").hasAuthority("Seller").
                antMatchers("/Buyer").hasAuthority("Buyer").
                antMatchers("/login/oauth2/code/google").permitAll().
                   antMatchers(HttpHeaders.ALLOW).permitAll().anyRequest().authenticated().and().oauth2Login().userInfoEndpoint().userService(customouth2userservice).and()
                .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint) .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
//    yjlfkgefyjlfkgefpncoqmah
@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}