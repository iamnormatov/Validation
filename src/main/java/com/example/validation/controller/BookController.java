package com.example.validation.controller;

import com.example.validation.dto.BookDto;
import com.example.validation.dto.ResponseDto;
import com.example.validation.dto.SimpleCRUD;
import com.example.validation.service.BookService;
import com.example.validation.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "book")
public class BookController implements SimpleCRUD<Integer, BookDto> {
    private final BookService bookService;

    @PostMapping(value = "/create")
    @Override
    public ResponseDto<BookDto> create(@RequestBody @Valid BookDto dto) {
        return this.bookService.create(dto);
    }

    @GetMapping(value = "/get/{id}")
    @Override
    public ResponseDto<BookDto> get(@PathVariable(value = "id") Integer entityId) {
        return this.bookService.get(entityId);
    }

    @PutMapping(value = "/update{id}")
    @Override
    public ResponseDto<BookDto> update(@RequestBody BookDto dto, @PathVariable(value = "id") Integer entityId) {
        return this.bookService.update(dto, entityId);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Override
    public ResponseDto<BookDto> delete(@PathVariable(value = "id") Integer entityId) {
        return this.bookService.delete(entityId);
    }

    @GetMapping(value = "/book-basic")
    public ResponseDto<Page<BookDto>> findByBookBasic(@RequestParam Map<String, String> params){
        return this.bookService.findByBookBasic(params);
    }
}
