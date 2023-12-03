package com.example.courseprojectnetology.service;

import com.example.courseprojectnetology.models.User;

import java.util.List;

public interface UserService {

   // User register(User user);

    List<User> getAll();

    User findByUsername(String username);

    User findById(Long id);

    void delete(Long id);
}
