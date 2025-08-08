package com.gestion.biblioteca.dto;

import com.gestion.biblioteca.domain.Autor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LibroResponseDTO {

    private String titulo;
    private Autor autor;
}
