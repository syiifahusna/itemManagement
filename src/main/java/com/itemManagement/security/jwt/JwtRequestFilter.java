package com.itemManagement.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itemManagement.enums.OperationStatusEnum;
import com.itemManagement.exceptionhandler.ApiNotExistException;
import com.itemManagement.payload.ResponseMessage;
import com.itemManagement.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        String requestURI = request.getRequestURI();
        try {
            if (requestURI.contains("/api/")) {
                if (requestURI.equals("/api/auth")) {
                    filterChain.doFilter(request, response);
                    return;
                }

                String username;
                String token;
                String authHeader = request.getHeader("Auth");

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    token = authHeader.substring(7);

                    if (jwtUtil.isTokenValidate(token)) {
                        username = jwtUtil.getUsernameFromToken(token);

                        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                            if (userDetails != null) {
                                UsernamePasswordAuthenticationToken authenticationToken =
                                        new UsernamePasswordAuthenticationToken(
                                                userDetails, null, userDetails.getAuthorities()
                                        );
                                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                            } else {
                                throw new RuntimeException("User not found");
                            }
                        } else {
                            throw new RuntimeException("Wrong token");
                        }
                    } else {
                        throw new RuntimeException("Token validation fail");
                    }
                } else {
                    throw new RuntimeException("No token found");
                }
            }

            filterChain.doFilter(request, response);

        }catch(Exception e){
            ResponseMessage responseMessage = new ResponseMessage(HttpStatus.NOT_FOUND,
                        OperationStatusEnum.FAILED, e.getMessage(), null);

            ObjectMapper objectMapper = new ObjectMapper();
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(responseMessage));
        }

    }
}
