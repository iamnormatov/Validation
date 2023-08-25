package com.example.validation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private Integer bookId;
    @NotBlank(message = "Firstname cannot be null or empty")
    private String bookName;
    @NotBlank(message = "Author cannot be null or empty")
    private String author;
    private LocalDateTime date;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
