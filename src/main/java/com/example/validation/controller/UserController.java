package com.example.validation.controller;

import com.example.validation.dto.ResponseDto;
import com.example.validation.dto.SimpleCRUD;
import com.example.validation.dto.UserDto;
import com.example.validation.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "user")
@RequiredArgsConstructor
public class UserController implements SimpleCRUD<Integer, UserDto> {

    private final UserService userService;

    @PostMapping(value = "/create")
    @Override
    @Operation(
            method = "method",
            summary = "summary",
            description = "it's first description "
    )
    @Tag(
            name = "ASDFDGFHH",
            description = "erertyghi"
    )
    public ResponseDto<UserDto> create(@RequestBody UserDto dto) {
        return this.userService.create(dto);
    }

    @GetMapping(value = "/get/{id}")
    @Override
    public ResponseDto<UserDto> get(@PathVariable(value = "id") Integer entityId) {
        return this.userService.get(entityId);
    }

    @PutMapping(value = "/update/{id}")
    @Override
    public ResponseDto<UserDto> update(@RequestBody UserDto dto, @PathVariable(value = "id") Integer entityId) {
        return this.userService.update(dto, entityId);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Override
    public ResponseDto<UserDto> delete(@PathVariable(value = "id") Integer entityId) {
        return this.userService.delete(entityId);
    }

    @GetMapping(value = "/find-by-user")
    public ResponseDto<Page<UserDto>> findByUser(@RequestParam Map<String, String> params){
        return this.userService.findByUser(params);
    }

    @GetMapping(value = "/search-advanced")
    public ResponseDto<Page<UserDto>> searchAdvanced(Map<String, String> params){
        return this.userService.searchAdvanced(params);
    }

    @GetMapping(value = "/sale")
    public ResponseDto<List<UserDto>> sale(){
        return this.userService.sale();
    }
}

