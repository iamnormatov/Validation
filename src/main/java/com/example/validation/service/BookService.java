package com.example.validation.service;

import com.example.validation.dto.BookDto;
import com.example.validation.dto.ResponseDto;
import com.example.validation.dto.SimpleCRUD;
import com.example.validation.model.Book;
import com.example.validation.repository.BookRepository;
import com.example.validation.service.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService implements SimpleCRUD<Integer, BookDto> {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public ResponseDto<BookDto> create(BookDto dto) {
        try {
            Book book = this.bookMapper.toEntity(dto);
            book.setCreatedAt(LocalDateTime.now());
            this.bookRepository.save(book);
            return ResponseDto.<BookDto>builder()
                    .success(true)
                    .message("OK")
                    .data(this.bookMapper.toDto(book))
                    .build();
        } catch (Exception e) {
            return ResponseDto.<BookDto>builder()
                    .code(-2)
                    .message(String.format("Book while saving error :: %s", e.getMessage()))
                    .build();
        }
    }

    @Override
    public ResponseDto<BookDto> get(Integer entityId) {
        return this.bookRepository.findByBookIdAndDeletedAtIsNull(entityId)
                .map(book -> ResponseDto.<BookDto>builder()
                        .success(true)
                        .message("OK")
                        .data(this.bookMapper.toDto(book))
                        .build()
                )
                .orElse(ResponseDto.<BookDto>builder()
                        .code(-1)
                        .message("Book is not found")
                        .build()
                );
    }

    @Override
    public ResponseDto<BookDto> update(BookDto dto, Integer entityId) {
        try {
            return this.bookRepository.findByBookIdAndDeletedAtIsNull(entityId)
                    .map(book -> {
                        this.bookMapper.toUpdate(dto, book);
                        book.setUpdatedAt(LocalDateTime.now());
                        this.bookRepository.save(book);
                        return ResponseDto.<BookDto>builder()
                                .success(true)
                                .message("OK")
                                .data(this.bookMapper.toDto(book))
                                .build();
                    })
                    .orElse(ResponseDto.<BookDto>builder()
                            .code(-1)
                            .message("Book is nt found")
                            .build()
                    );
        } catch (Exception e) {
            return ResponseDto.<BookDto>builder()
                    .code(-2)
                    .message(String.format("Book while updating error :: %s", e.getMessage()))
                    .build();
        }
    }

    @Override
    public ResponseDto<BookDto> delete(Integer entityId) {
        return this.bookRepository.findByBookIdAndDeletedAtIsNull(entityId)
                .map(book -> {
                    book.setDeletedAt(LocalDateTime.now());
                    this.bookRepository.save(book);
                    return ResponseDto.<BookDto>builder()
                            .success(true)
                            .message("OK")
                            .data(this.bookMapper.toDto(book))
                            .build();
                })
                .orElse(ResponseDto.<BookDto>builder()
                        .code(-1)
                        .message("Book is not found")
                        .build()
                );
    }

    public ResponseDto<Page<BookDto>> findByBookBasic(Map<String, String> params) {
        int page = 0, size = 10;
        if (params.containsKey("page")) {
            page = Integer.parseInt(params.get("page"));
        }
        if (params.containsKey("page")) {
            size = Integer.parseInt(params.get("size"));
        }
        Page<Book> books = this.bookRepository.searchAllBook(
                params.get("id") == null ? null : Integer.parseInt(params.get("id")),
                params.get("b"), params.get("a"),
                PageRequest.of(page, size)
        );
        return ResponseDto.<Page<BookDto>>builder()
                .success(true)
                .message("OK")
                .data(books.map(this.bookMapper::toDto))
                .build();
    }
}
