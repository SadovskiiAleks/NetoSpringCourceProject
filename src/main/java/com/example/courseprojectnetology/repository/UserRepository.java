package com.example.courseprojectnetology.repository;

import com.example.courseprojectnetology.models.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String name);
}
