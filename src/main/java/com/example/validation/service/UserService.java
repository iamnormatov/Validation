package com.example.validation.service;

import com.example.validation.dto.*;
import com.example.validation.model.Authorities;
import com.example.validation.model.User;
import com.example.validation.model.UserAccessSession;
import com.example.validation.model.UserRefreshSession;
import com.example.validation.repository.AuthoritiesRepository;
import com.example.validation.repository.UserAccessRepository;
import com.example.validation.repository.UserRefreshRepository;
import com.example.validation.repository.UserRepository;
import com.example.validation.security.JwtUtils;
import com.example.validation.service.mapper.UserMapper;
import com.example.validation.service.validation.UserValidation;
import com.example.validation.util.UserRepositoryImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final AuthoritiesRepository authoritiesRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidation userValidation;
    private final UserRepositoryImpl userRepositoryImpl;
    private final JwtUtils jwtUtils;
    private final UserRefreshRepository userRefreshRepository;
    private final UserAccessRepository userAccessRepository;

    public ResponseDto<UserDto> register(UserDto dto) {
        List<ErrorDto> errors = this.userValidation.validate(dto);
        if (!errors.isEmpty()) {
            return ResponseDto.<UserDto>builder()
                    .code(-3)
                    .error(errors)
                    .build();
        }
        try {
            var entity = this.userMapper.toEntity(dto);
            entity.setCreatedAt(LocalDateTime.now());
            User saveUser = this.userRepository.save(entity);
            this.authoritiesRepository.save(Authorities.builder()
                    .userId(saveUser.getUserId())
                    .authority("user")
                    .username(saveUser.getUsername())
                    .build());
            return ResponseDto.<UserDto>builder()
                    .success(true)
                    .message("OK")
                    .data(this.userMapper.toDto(saveUser))
                    .build();
        } catch (Exception e) {
            return ResponseDto.<UserDto>builder()
                    .message(String.format("User while saving error :: %s", e.getMessage()))
                    .code(-2)
                    .build();
        }
    }

    @Transactional
    public ResponseDto<TokenResponseDto> registerConfirm(RegisterConfirmDto dto) {
        return this.userRepository.findByUsernameAndDeletedAtIsNull(dto.getUsername())
                .map(user -> {
                    User saveUser = this.userRepository.save(user);
                    String newSubject = toJsonByUser(saveUser);

                    checkValidToken(newSubject);

                    saveUserSession(newSubject, this.userMapper.toDto(user));

                    return ResponseDto.<TokenResponseDto>builder()
                            .success(true)
                            .message("OK")
                            .data(TokenResponseDto.builder()
                                    .accessToken(this.jwtUtils.generateToken(newSubject))
                                    .refreshToken(this.jwtUtils.generateToken(newSubject))
                                    .build())
                            .build();
                })
                .orElse(ResponseDto.<TokenResponseDto>builder()
                        .code(-1)
                        .message(String.format("User %s is not found", dto.getUsername()))
                        .build());
    }

    public ResponseDto<TokenResponseDto> login(LoginDto dto) {
        return this.userRepository.findByUsernameAndEnabledIsTrueAndDeletedAtIsNull(dto.getUsername())
                .map(user -> {
                    String newSubject = toJsonByUser(user);
                    checkValidToken(newSubject);

                    saveUserSession(newSubject, this.userMapper.toDto(user));

                    this.userAccessRepository.save(new UserAccessSession(
                            newSubject,
                            this.userMapper.toDto(user))
                    );
                    this.userRefreshRepository.save(new UserRefreshSession(
                            newSubject,
                            this.userMapper.toDto(user))
                    );
                    return ResponseDto.<TokenResponseDto>builder()

                            .build();
                })
                .orElse(ResponseDto.<TokenResponseDto>builder()
                        .code(-1)
                        .message(String.format("User %s is not found", dto.getUsername()))
                        .build());
    }

    public ResponseDto<TokenResponseDto> refreshToken(String token) {
        if (!this.jwtUtils.isValid(token)) return ResponseDto.<TokenResponseDto>builder()
                .code(-3)
                .message("Token is not valid!")
                .build();

        return this.userRefreshRepository.findById(token)
                .map(userRefreshSession -> {

                    checkValidToken(token);

                    UserDto userDto = userRefreshSession.getUserDto();

                    User userEntity = this.userMapper.toEntity(userDto);
                    userEntity.setEnabled(true);
                    String newSubject = toJsonByUser(userEntity);

                    saveUserSession(newSubject, this.userMapper.toDto(userEntity));

                    var newToken = this.jwtUtils.generateToken(newSubject);

                    return ResponseDto.<TokenResponseDto>builder()
                            .success(true)
                            .message("OK")
                            .data(TokenResponseDto.builder()
                                    .accessToken(newToken)
                                    .refreshToken(newToken)
                                    .build())
                            .build();
                })
                .orElse(ResponseDto.<TokenResponseDto>builder()
                        .code(-1)
                        .message(String.format("User with token %s is not found!", token))
                        .build()
                );
    }

    public ResponseDto<UserDto> logout(RegisterConfirmDto dto) {
        return this.userRepository.findByUsernameAndEnabledIsTrueAndDeletedAtIsNull(dto.getUsername())
                .map(user -> {
                    user.setEnabled(false);
                    this.userRepository.save(user);
                    return ResponseDto.<UserDto>builder()
                            .success(true)
                            .message("OK")
                            .build();
                })
                .orElse(ResponseDto.<UserDto>builder()
                        .code(-1)
                        .message(String.format("User with %s username is not found!", dto.getUsername()))
                        .build()
                );
    }

    private void checkValidToken(String token) {
        this.userAccessRepository.findById(token)
                .ifPresent(this.userAccessRepository::delete);

        this.userRefreshRepository.findById(token)
                .ifPresent(this.userRefreshRepository::delete);
    }

    private void saveUserSession(String sessionId, UserDto userDto) {
        this.userAccessRepository.save(new UserAccessSession(
                        sessionId, userDto
                )
        );

        this.userRefreshRepository.save(new UserRefreshSession(
                        sessionId, userDto
                )
        );
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
        if (userList.isEmpty()) {
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


    public String toJsonByUser(User dto) {
        return "userId-" + dto.getUserId() +
                ", firstName-'" + dto.getFirstName() + '\'' +
                ", lastName-'" + dto.getLastName() + '\'' +
                ", username-'" + dto.getUsername() + '\'' +
                ", enabled-" + dto.getEnabled();
    }


}

