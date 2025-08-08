package com.gestion.biblioteca.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponseDTO {

    private LocalDateTime timestamp;
    private String message;
    private int status;
    private String path;
}
