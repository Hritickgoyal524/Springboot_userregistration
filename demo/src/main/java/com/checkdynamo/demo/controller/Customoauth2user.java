package com.checkdynamo.demo.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
//Spring boot related config
public class Customoauth2user implements OAuth2User {
    private OAuth2User oAuth2User;

    public Customoauth2user(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }



    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }


    @Override
    public String getName() {
        return oAuth2User.getAttribute("name");
    }
    public String getEmail() {
        return oAuth2User.getAttribute("email");
    }
}
