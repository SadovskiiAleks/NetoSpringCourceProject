package com.example.courseprojectnetology.exception.errors;

import com.example.courseprojectnetology.exception.BaseApplicationException;

public class ErrorDeleteFile extends BaseApplicationException {
    public ErrorDeleteFile(String msg, int id) {
        super(msg, id);
    }

    public int getId() {
        return super.getId();
    }
}
