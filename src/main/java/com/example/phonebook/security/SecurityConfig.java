package com.example.phonebook.security;

import com.example.phonebook.service.JwtConfigurer;
import com.example.phonebook.service.JwtTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
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
                        auth.requestMatchers("/api/login", "/api/registration","/registration", "/login").permitAll()
                                .anyRequest().authenticated()
                                .and()

                )
                .formLogin().loginPage("/login")
                .successForwardUrl("/users/contacts")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .permitAll()
                .and()
                .httpBasic(Customizer.withDefaults())
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
