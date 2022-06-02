package com.checkdynamo.demo.config;

import com.checkdynamo.demo.Util.JwtUtil;
import com.checkdynamo.demo.service.Jwtservice;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class
JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private Jwtservice jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String userName= null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                userName = jwtUtil.getUsernameFromToken(jwtToken);
                System.out.println("useremailfsdg      "+userName);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
            catch (Exception e){
                System.out.println("fsifnefg");
                e.printStackTrace();
            }
        } else {
            System.out.println("JWT token does not start with Bearer");
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("fsifbfhbfhbhbhbhbvhbnefg");
            UserDetails userDetails = jwtService.loadUserByUsername(userName);

            if (jwtUtil.validateToken(jwtToken, userDetails)) {
                System.out.println("fsifnefg111111111");
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);

    }

}