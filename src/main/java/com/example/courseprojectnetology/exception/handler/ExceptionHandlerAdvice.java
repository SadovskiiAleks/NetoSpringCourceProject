package com.example.courseprojectnetology.exception.handler;

import com.example.courseprojectnetology.exception.dto.ErrorDTO;
import com.example.courseprojectnetology.exception.errors.InternetServerError;
import com.example.courseprojectnetology.exception.errors.BadRequestError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(BadRequestError.class)
    public ResponseEntity<ErrorDTO> errorInputData(BadRequestError badRequestError) {
        ErrorDTO errorDto = createErrorDto(badRequestError);
        return ResponseEntity.badRequest()
                .body(errorDto);
    }

    public ErrorDTO createErrorDto(BadRequestError badRequestError) {
        String massage = badRequestError.getMessage();
        int id = badRequestError.getId();
        return new ErrorDTO(massage, id);
    }

    @ExceptionHandler(InternetServerError.class)
    public ResponseEntity<ErrorDTO> errorInputData(InternetServerError internetServerError) {
        ErrorDTO errorDto = createErrorDto(internetServerError);
        return ResponseEntity.internalServerError()
                .body(errorDto);
    }

    public ErrorDTO createErrorDto(InternetServerError internetServerError) {
        String massage = internetServerError.getMessage();
        int id = internetServerError.getId();
        return new ErrorDTO(massage, id);
    }

}


