package com.example.validation.service;

import com.example.validation.dto.ErrorDto;
import com.example.validation.dto.ResponseDto;
import com.example.validation.dto.SimpleCRUD;
import com.example.validation.dto.UserDto;
import com.example.validation.model.User;
import com.example.validation.repository.UserRepository;
import com.example.validation.service.mapper.UserMapper;
import com.example.validation.service.validation.UserValidation;
import com.example.validation.util.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidation userValidation;
    private final UserRepositoryImpl userRepositoryImpl;

    public ResponseDto<UserDto> create(UserDto dto) {
        List<ErrorDto> errors = this.userValidation.validate(dto);
        if (!errors.isEmpty()) {
            return ResponseDto.<UserDto>builder()
                    .code(-3)
                    .error(errors)
                    .build();
        }
        try {
            User user = this.userMapper.toEntity(dto);
            user.setCreatedAt(LocalDateTime.now());
            this.userRepository.save(user);
            return ResponseDto.<UserDto>builder()
                    .success(true)
                    .message("OK")
                    .data(this.userMapper.toDto(user))
                    .build();
        } catch (Exception e) {
            return ResponseDto.<UserDto>builder()
                    .message(String.format("User while saving error :: %s", e.getMessage()))
                    .code(-2)
                    .build();
        }
    }

    public ResponseDto<UserDto> get(Integer entityId) {
        Optional<User> optional = this.userRepository.findByUserIdAndDeletedAtIsNull(entityId);
        if (optional.isEmpty()) {
            return ResponseDto.<UserDto>builder()
                    .message("User is not found")
                    .code(-1)
                    .build();
        }
        return ResponseDto.<UserDto>builder()
                .success(true)
                .message("OK")
                .data(this.userMapper.toDto(optional.get()))
                .build();
    }

    public ResponseDto<UserDto> update(UserDto dto, Integer entityId) {
        List<ErrorDto> errors = this.userValidation.validate(dto);
        if (!errors.isEmpty()) {
            return ResponseDto.<UserDto>builder()
                    .code(-3)
                    .error(errors)
                    .build();
        }

        Optional<User> optional = this.userRepository.findByUserIdAndDeletedAtIsNull(entityId);
        if (optional.isEmpty()) {
            return ResponseDto.<UserDto>builder()
                    .code(-1)
                    .message("User is not found")
                    .build();
        }

        try {
            User user = optional.get();
            this.userMapper.update(dto, user);
            user.setUpdatedAt(LocalDateTime.now());
            this.userRepository.save(user);
            return ResponseDto.<UserDto>builder()
                    .success(true)
                    .message("OK")
                    .data(this.userMapper.toDto(user))
                    .build();
        } catch (Exception e) {
            return ResponseDto.<UserDto>builder()
                    .message(String.format("User while saving error %s ", e.getMessage()))
                    .code(-2)
                    .build();
        }
    }

    public ResponseDto<UserDto> delete(Integer entityId) {
        Optional<User> optional = this.userRepository.findByUserIdAndDeletedAtIsNull(entityId);
        if (optional.isEmpty()) {
            return ResponseDto.<UserDto>builder()
                    .code(-1)
                    .message("User is not found")
                    .build();
        }
        User user = optional.get();
        user.setDeletedAt(LocalDateTime.now());
        this.userRepository.save(user);
        return ResponseDto.<UserDto>builder()
                .success(true)
                .message("OK")
                .data(this.userMapper.toDto(optional.get()))
                .build();
    }

    public ResponseDto<Page<UserDto>> findByUser(Map<String, String> params) {
        int page = 0, size = 10;
        if (params.containsKey("page")) {
            page = Integer.parseInt(params.get("page"));
        }
        if (params.containsKey("size")) {
            size = Integer.parseInt(params.get("size"));
        }
        return Optional.ofNullable(this.userRepository.findAllUser(
                        Integer.valueOf(params.get("id")),
                        params.get("f"), params.get("l"),
                        Integer.valueOf(params.get("a")),
                        params.get("e"), PageRequest.of(page, size)
                ))
                .map(users -> ResponseDto.<Page<UserDto>>builder()
                        .success(true)
                        .message("OK")
                        .data(null)
                        .build()
                )
                .orElse(ResponseDto.<Page<UserDto>>builder()
                        .code(-1)
                        .message(String.format("User with %s params are not found!", params))
                        .build()
                );
    }


    public ResponseDto<Page<UserDto>> searchAdvanced(Map<String, String> params) {
        return Optional.of(this.userRepositoryImpl.advanced(params)
                        .map(this.userMapper::toDto))
                .map(user -> ResponseDto.<Page<UserDto>>builder()
                        .success(true)
                        .message("OK")
                        .data(user)
                        .build()
                )
                .orElse(ResponseDto.<Page<UserDto>>builder()
                        .code(-1)
                        .message("User are not found!")
                        .build()
                );
    }

    public ResponseDto<List<UserDto>> sale() {
        List<User> userList = this.userRepository.findAllByDeletedAtIsNull();
        if (userList.isEmpty()){
            return ResponseDto.<List<UserDto>>builder()
                    .code(-1)
                    .message("User are not found")
                    .build();
        }
        return ResponseDto.<List<UserDto>>builder()
                .success(true)
                .message("OK")
                .data(userList.stream().map(this.userMapper::toDto).toList())
                .build();
    }

    @Override
    public UserDto loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsernameAndEnabledIsTrueAndDeletedAtIsNull(username)
                .map(this.userMapper::toDto)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Auth with %s :: username is not found", username)));
    }
}

