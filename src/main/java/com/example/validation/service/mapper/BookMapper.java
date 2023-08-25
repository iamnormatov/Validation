package com.example.validation.service.mapper;

import com.example.validation.dto.BookDto;
import com.example.validation.model.Book;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class BookMapper {
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    public abstract Book toEntity(BookDto bookDto);

    public abstract BookDto toDto(Book book);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void toUpdate(BookDto bookDto, @MappingTarget Book book);
}
