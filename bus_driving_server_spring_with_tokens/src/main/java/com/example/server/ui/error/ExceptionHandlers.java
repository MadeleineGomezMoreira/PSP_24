package com.example.server.ui.error;

import com.example.server.domain.exception.TokenInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<ApiError> handleValidationException(TokenInvalidException e) {
        ApiError apiError = new ApiError(e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }

//    @ExceptionHandler(CertificateException.class)
//    public ResponseEntity<ApiError> handleValidationException(CertificateException e) {
//        ApiError apiError = new ApiError(e.getMessage(), LocalDateTime.now());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
//    }

}
