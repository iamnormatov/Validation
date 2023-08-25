package com.example.validation.repository;

import com.example.validation.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserIdAndDeletedAtIsNull(Integer entityId);

    boolean existsByEmail(String email);

    @Query(name = "search")
    Page<User> findAllUser(
            @Param(value = "id") Integer userId,
            @Param(value = "f") String firstName,
            @Param(value = "l") String lastName,
            @Param(value = "a") Integer age,
            @Param(value = "e") String email,
            Pageable pageable);


    List<User> findAllByDeletedAtIsNull();
}