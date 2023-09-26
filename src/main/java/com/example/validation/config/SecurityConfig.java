package com.example.validation.config;

import com.example.validation.security.SecurityFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final SecurityFilter filter;

//    private final DataSource dataSource;
//
//    @Autowired
//    public void authenticationManagerBuilder(AuthenticationManagerBuilder builder) throws Exception {
//        builder.jdbcAuthentication()
//                .usersByUsernameQuery("select username from users where username = ?")
//                .dataSource(dataSource)
//                .passwordEncoder(passwordEncoder);
//    }

//    @Autowired
//    public void authenticationManagerBuilder(AuthenticationManagerBuilder builder) throws Exception {
//        builder.inMemoryAuthentication()
//                .withUser("Nodirxon")
//                .password(passwordEncoder.encode("root"))
//                .roles("Admin").and()
//                .withUser("Gulnora")
//                .password(passwordEncoder.encode("data"))
//                .roles("Admin").and()
//                .passwordEncoder(passwordEncoder);
//    }

    @Bean
    public SecurityFilterChain filterChainChain(HttpSecurity http) throws Exception {
        String[] access = {
                "/user/**",
                "/swagger-ui/**",
                "/swagger-resources/*",
                "*.html",
                "/api/v1/swagger.json"
        };

        String[] users = {"/user/create", "/user/get"};
        return http
                .csrf().disable()
                .cors().disable()
                .authorizeHttpRequests()
                .requestMatchers(access).permitAll()
                .anyRequest()
                .authenticated().and()
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/v3/api-docs/**");
    }

}
