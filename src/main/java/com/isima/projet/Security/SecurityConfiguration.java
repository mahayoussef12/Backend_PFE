package com.isima.projet.Security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/**",
            "api/v1/demande_insc",
            "/api/v1/client/**",
            "/api/v1/entreprise/**",
            "/api/v1/avis/**",
            "api/v1/user/ajouter",
            "/notification/token","/chat",
            "/api/events/create",
            "/api/**/**/**",
            "/login"



    };

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {


        auth
                .inMemoryAuthentication()
                .withUser("/")
                .password(("/"))
                .roles("/")
                .and();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http
                .cors().and().csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(PUBLIC_ENDPOINTS).permitAll()
                .antMatchers("/swagger-ui/**", "/pfe/**").permitAll()
                .anyRequest().authenticated();

    }

/*
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/swagger-ui/**", "/javainuse-openapi/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }*/
}
