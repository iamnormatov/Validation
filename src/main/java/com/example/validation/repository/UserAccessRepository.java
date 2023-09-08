package com.example.validation.repository;

import com.example.validation.model.UserAccessSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccessRepository extends JpaRepository<UserAccessSession, String> {

}
