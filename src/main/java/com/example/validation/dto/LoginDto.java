package com.example.validation.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class LoginDto {
    private String username;
    @Value(value = "0000")
    private String code; // code -> 0000
}
