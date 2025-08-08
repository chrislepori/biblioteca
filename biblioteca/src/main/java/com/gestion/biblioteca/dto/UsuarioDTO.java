package com.gestion.biblioteca.dto;

import com.gestion.biblioteca.domain.Rol;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioDTO {

    @NotBlank(message = "El dni no puede ser nulo ni estar vacío")
    private String dni;
    @NotBlank(message = "El nombre no puede ser nulo ni estar vacío")
    private String nombreCompleto;
    @NotBlank(message = "El mail no puede ser nulo ni estar vacío")
    private String mail;
    @NotBlank(message = "El password no puede ser nulo ni estar vacío")
    private String passWord;
    @NotNull(message = "El rol no puede ser nulo")
    private Rol rol;


}
