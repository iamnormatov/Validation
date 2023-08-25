package com.example.validation.repository;

import com.example.validation.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findByBookIdAndDeletedAtIsNull(Integer entityId);

    @Query(value = "select b from Book as b where " +
            "coalesce(:id, b.bookId) = b.bookId " +
            "or coalesce(:b, b.bookName) = b.bookName " +
            "or coalesce(:a, b.author) = b.author " +
            "and b.deletedAt is null ")
    Page<Book> searchAllBook(
            @Param(value = "id") Integer bookId,
            @Param(value = "b") String bookName,
            @Param(value = "a") String author,
            Pageable pageable
    );
}
