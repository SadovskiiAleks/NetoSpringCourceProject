package com.example.courseprojectnetology.repository;

import com.example.courseprojectnetology.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
