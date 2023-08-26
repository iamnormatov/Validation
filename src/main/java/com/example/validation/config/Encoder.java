package com.example.validation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class Encoder {

//    @Value(value = "${spring.datasource.url}")
//    private String url;
//
//    @Value(value = "${spring.datasource.username}")
//    private String username;
//
//    @Value(value = "${spring.datasource.password}")
//    private String password;
    @Bean
    public PasswordEncoder password() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public DataSource dataSource(){
//        SimpleDriverDataSource sdds = new SimpleDriverDataSource();
//        sdds.setUrl(url);
//        sdds.setUsername(username);
//        sdds.setPassword(password);
//        sdds.setDriverClass(org.postgresql.Driver.class);
//        return sdds;
//    }
}
