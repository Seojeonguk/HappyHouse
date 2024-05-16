package com.example.happyhouse.domain.repository;

import com.example.happyhouse.domain.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByLoginId(String loginId);
}
