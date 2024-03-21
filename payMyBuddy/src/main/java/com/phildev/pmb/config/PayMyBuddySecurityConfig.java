package com.phildev.pmb.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
public class PayMyBuddySecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.authorizeHttpRequests(auth ->{
            auth.requestMatchers( "/login", "/register","/index", "/registration-success","/login-check", "403").permitAll();
            auth.requestMatchers("/admin", "/transfer-management", "/user-management").hasRole("ADMIN");
            auth.requestMatchers("/home").hasRole("USER");
            auth.anyRequest().authenticated();
        }).formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(new CustomSuccessHandler())
                        .failureUrl("/login?error=true"))
            .oauth2Login(form-> form
                    .loginPage("/login")
                    .defaultSuccessUrl("/login-check")
                    .failureUrl("/login?error=true"))
        .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    WebSecurityCustomizer enableStaticResources(){
        return (web -> web.ignoring().requestMatchers("/css/**", "js/**","/images/**"));
    }


}
