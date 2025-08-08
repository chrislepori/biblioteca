package com.gestion.biblioteca.exception;

import com.gestion.biblioteca.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponseDTO> handleApiException(ApiException apiException, HttpServletRequest httpServletRequest){
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(LocalDateTime.now(), apiException.getError().getMensaje(),
                apiException.getError().getCodigo().value(), httpServletRequest.getRequestURI() );
        return ResponseEntity.status(apiException.getError().getCodigo()).body(errorResponseDTO);


    }

}
