package com.example.validation.repository;

import com.example.validation.model.UserRefreshSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRefreshRepository extends JpaRepository<UserRefreshSession, String> {

}
