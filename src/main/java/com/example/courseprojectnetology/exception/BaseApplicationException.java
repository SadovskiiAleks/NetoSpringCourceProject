package com.example.courseprojectnetology.exception;

public class BaseApplicationException extends RuntimeException {
    private final int id;

    public BaseApplicationException(String msg, int id) {
        super(msg);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}