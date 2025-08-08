package com.gestion.biblioteca.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrestamoDTO {

    @NotNull(message = "El id no puede ser nulo")
    private Long libroId;
    @NotNull(message = "El id no puede ser nulo")
    private Long usuarioId;

}
