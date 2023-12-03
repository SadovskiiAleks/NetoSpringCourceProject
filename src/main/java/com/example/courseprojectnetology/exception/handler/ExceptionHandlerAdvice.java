package com.example.courseprojectnetology.exception.handler;

import com.example.courseprojectnetology.exception.dto.ErrorDTO;
import com.example.courseprojectnetology.exception.errors.InternalServerError;
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

    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<ErrorDTO> errorInputData(InternalServerError internalServerError) {
        ErrorDTO errorDto = createErrorDto(internalServerError);
        return ResponseEntity.internalServerError()
                .body(errorDto);
    }

    public ErrorDTO createErrorDto(InternalServerError internalServerError) {
        String massage = internalServerError.getMessage();
        int id = internalServerError.getId();
        return new ErrorDTO(massage, id);
    }

}


