package com.example.courseprojectnetology.exception.handler;

import com.example.courseprojectnetology.exception.dto.ErrorDTO;
import com.example.courseprojectnetology.exception.errors.ErrorUploadFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ErrorUploadFile.class)
    public ResponseEntity<ErrorDTO> errorInputData(ErrorUploadFile errorUploadFile) {
        ErrorDTO errorDto = createErrorDto(errorUploadFile);
        return ResponseEntity.badRequest()
                .body(errorDto);
    }

    public ErrorDTO createErrorDto(ErrorUploadFile errorUploadFile) {
        String massage = errorUploadFile.getMessage();
        int id = errorUploadFile.getId();
        return new ErrorDTO(massage, id);
    }

}


