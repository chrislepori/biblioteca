package com.gestion.biblioteca.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LibroDTO {

    @NotNull(message = "El isbn no puede ser null")
    private String isbn;
    @Min(value = 1, message = "La cantidad inicial no puede ser cero ni negativa")
    private int cantidadInicial;
}
