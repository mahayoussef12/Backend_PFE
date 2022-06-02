package com.isima.projet.Security;

import com.isima.projet.User.implementation.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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







/*    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .and().csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(PUBLIC_ENDPOINTS).permitAll()
                .antMatchers("/swagger-ui/**", "/pfe/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .and()
                .logout().permitAll();


    }*/
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImpl();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(encoder());

        return authProvider;
    }
    @Bean
    public PasswordEncoder encoder() {

        return new BCryptPasswordEncoder();

    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/api/**/**/**").hasRole("entreprise")
                .antMatchers("/api/**/**/**").hasRole("client")
                .antMatchers("/api/**/**/**").permitAll()
                .antMatchers("/swagger-ui/**", "/pfe/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                //.defaultSuccessUrl("/admin")
                .and()
                .logout().permitAll();
    }
}
