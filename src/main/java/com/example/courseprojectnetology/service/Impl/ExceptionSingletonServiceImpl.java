package com.example.courseprojectnetology.service.Impl;

import org.springframework.stereotype.Service;

@Service
public class ExceptionSingletonServiceImpl {

    private static ExceptionSingletonServiceImpl instance;
    private int id = 1;

    public static synchronized ExceptionSingletonServiceImpl getInstance() {
        if (instance == null) {
            instance = new ExceptionSingletonServiceImpl();
        }
        return instance;
    }

    public synchronized int getId() {
        id++;
        return id;
    }
}
