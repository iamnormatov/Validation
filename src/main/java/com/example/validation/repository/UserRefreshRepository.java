package com.example.validation.repository;

import com.example.validation.model.UserRefreshSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRefreshRepository extends CrudRepository<UserRefreshSession, String> {

}
