package com.gestion.biblioteca.dto;

import com.gestion.biblioteca.domain.Libro;
import com.gestion.biblioteca.domain.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrestamoResponseDTO {

    private LocalDate fechaPrestamo;
    private LocalDate fechaLimite;
    private LocalDate fechaRealDevolucion;
    private Libro libro;
    private Usuario usuario;

}
