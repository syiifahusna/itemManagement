package com.itemManagement.security;

import com.itemManagement.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher("/*","/admin/**", "/employee/**", "/client/**", "/user/**")
                .authorizeHttpRequests((auth) -> {
                    auth
                            .requestMatchers("/login","/register","/reset_password","/account_confirmation").anonymous() // Accessible only if not authenticated
                            .requestMatchers("/*", "/logout", "/assets/**").permitAll() // Public access
                            .requestMatchers("/admin/**").hasAuthority("ADMIN") // Admin only access
                            .requestMatchers("/employee/**").hasAnyAuthority("ADMIN", "EMPLOYEE") // Admin or Employee access
                            .requestMatchers("/client/**").hasAuthority("CLIENT") // Client only access
                            .requestMatchers("/user/**").hasAnyAuthority("ADMIN", "EMPLOYEE", "CLIENT") // Admin, Employee, or Client access
                            .anyRequest().authenticated(); // All other requests require authentication
                })
                .formLogin((form) -> {
                    form
                            .loginPage("/login")
                            .successHandler(customAuthSuccessHandler())
                            .failureHandler(customAuthFailureHandler());
                })
                .logout((logout) -> {
                    logout
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/login?msg=You_have_logged_out")
                            .clearAuthentication(true)
                            .deleteCookies("JSESSIONID")
                            .invalidateHttpSession(true)
                            .permitAll();
                })
                .rememberMe(remember->{
                    remember
                            .rememberMeParameter("rememberMe")
                            .tokenValiditySeconds(604800); // Set token validity (7 days)
                })
                .exceptionHandling(exception->{
                    exception
                            .accessDeniedPage("/access_denied");
                });

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthSuccessHandler() {
       return new CustomAuthSuccessHandler();
    }

    @Bean
    AuthenticationFailureHandler customAuthFailureHandler(){
        return new CustomAuthFailureHandler();
    }
}
