package com.example.courseprojectnetology.security.jwtMy.token.exeption;


import org.springframework.security.core.AuthenticationException;

public class JWTException extends AuthenticationException {

    public JWTException(String msg, Throwable t) {
        super(msg, t);
    }

    public JWTException(String msg) {
        super(msg);
    }
}
