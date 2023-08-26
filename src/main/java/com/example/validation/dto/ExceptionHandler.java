package com.example.validation.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ExceptionHandler {
//    @ExceptionHandler
//    public RequestEntity<ResponseDto<Void>> usernameNotFoundException(UsernameNotFoundException e){
//        return new RequestEntity<>(ResponseDto.<Void>builder()
//                .code(-1)
//                .message(String.format("Rejaction value: %s", e.getMessage()))
//                .build(), HttpStatus.NOT_FOUND);
//    }

}

