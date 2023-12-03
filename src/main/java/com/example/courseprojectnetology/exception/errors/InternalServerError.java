package com.example.courseprojectnetology.exception.errors;

import com.example.courseprojectnetology.exception.BaseApplicationException;

public class InternalServerError extends BaseApplicationException {
    public InternalServerError(String msg, int id) {
        super(msg, id);
    }

    public int getId() {
        return super.getId();
    }
}
