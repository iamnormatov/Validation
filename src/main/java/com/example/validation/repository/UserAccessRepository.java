package com.example.validation.repository;

import com.example.validation.model.UserAccessSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccessRepository extends CrudRepository<UserAccessSession, String> {

}
