package com.example.validation.repository;

import com.example.validation.model.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities, Integer> {
}
