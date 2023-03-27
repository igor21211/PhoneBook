package com.example.phonebook.security;

import com.example.phonebook.service.JwtConfigurer;
import com.example.phonebook.service.JwtTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, JwtTokenService jwtTokenService) throws Exception {
        http.authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/login", "/api/registration").permitAll()
                                .requestMatchers("/", "/users").hasAuthority("registry")
                                .requestMatchers("/api/users/contact/**").hasAuthority("registry")
                                .anyRequest().authenticated()
                                .and()

                )
                .formLogin().loginPage("/login").permitAll()
                .defaultSuccessUrl("/users/contacts")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .logoutSuccessUrl("/login")
                .and()
                .csrf()
                .disable()
                .apply(new JwtConfigurer(jwtTokenService));

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
