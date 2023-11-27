package com.example.courseprojectnetology.exception.errors;

import com.example.courseprojectnetology.exception.BaseApplicationException;

public class BadRequestError extends BaseApplicationException {
    public BadRequestError(String msg, int id) {
        super(msg, id);
    }

    public int getId() {
        return super.getId();
    }
}
