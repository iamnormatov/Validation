package com.example.validation.service.validation;

import com.example.validation.dto.ErrorDto;
import com.example.validation.dto.UserDto;
import com.example.validation.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserValidation {
    private final UserRepository userRepository;
    public List<ErrorDto> validate(UserDto userDto) {
        List<ErrorDto> list = new ArrayList<>();
        if (StringUtils.isBlank(userDto.getFirstName())) {
            list.add(new ErrorDto("firstName", "Firstname cannot be null or empty"));
        }
        if (StringUtils.isBlank(userDto.getLastName())) {
            list.add(new ErrorDto("lastName", "Lastname cannot be null or empty"));
        }
        if (StringUtils.isBlank(userDto.getEmail())){
          list.add(new ErrorDto("email", "Email cannot be null or empty"));
        } else if (this.userRepository.existsByEmail(userDto.getEmail())) {
            list.add(new ErrorDto("email", String.format("This email: %s already exist! ", userDto.getEmail())));
        }
        if (checkEmailPattern(userDto.getEmail())){
            list.add(new ErrorDto("email", String.format("Given %s The email was not valid", userDto.getEmail())));
        }
        return list;
    }

    public boolean checkEmailPattern(String email){
        try {
            String[] array = email.split("@");
            if (array.length == 2){
                return !array[1].equals("gmail.com");
            }
            return true;
        }catch (Exception e){
            return Boolean.parseBoolean(String.format("Email is not null %s", e.getMessage()));
        }

    }
}
