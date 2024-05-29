package com.example.server.ui.error;

import com.example.server.domain.exception.InvalidTokenException;
import com.example.server.domain.exception.MailMessagingException;
import com.example.server.domain.exception.NotFoundException;
import com.example.server.domain.exception.TokenInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.cert.CertificateException;
import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<ApiError> handleValidationException(TokenInvalidException e) {
        ApiError apiError = new ApiError(e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiError> handleValidationException(InvalidTokenException e) {
        ApiError apiError = new ApiError(e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }

    @ExceptionHandler(CertificateException.class)
    public ResponseEntity<ApiError> handleValidationException(CertificateException e) {
        ApiError apiError = new ApiError(e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleValidationException(NotFoundException e) {
        ApiError apiError = new ApiError(e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    @ExceptionHandler(MailMessagingException.class)
    public ResponseEntity<ApiError> handleValidationException(MailMessagingException e) {
        ApiError apiError = new ApiError(e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<ApiError> handleValidationException(MailException e) {
        ApiError apiError = new ApiError(e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }


}
