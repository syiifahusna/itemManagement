package com.itemManagement.service;

import com.itemManagement.entity.User;
import com.itemManagement.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findUserByUsername(username);
        if(optionalUser.isPresent()){
            return optionalUser.get();
        }

        throw new UsernameNotFoundException("Username not found");
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        if(optionalUser.isPresent()){
            return optionalUser.get();
        }

        throw new UsernameNotFoundException("Email not found");
    }

    public void updateUserDetails(String username) {
        // Fetch the current authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            // If authentication is null or not authenticated, skip updating security context.
            return;
        }

        // Update the UserDetails with the new information
        UserDetails updatedUserDetails = loadUserByUsername(username);

        // Create a new authentication token using the updated details
        UsernamePasswordAuthenticationToken newAuthentication =
                new UsernamePasswordAuthenticationToken(
                        updatedUserDetails,
                        authentication.getCredentials(),
                        updatedUserDetails.getAuthorities()
                );

        // Update the security context with the new authentication
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
    }
}
