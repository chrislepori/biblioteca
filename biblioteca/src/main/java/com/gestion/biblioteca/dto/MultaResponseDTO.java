package com.gestion.biblioteca.dto;

import com.gestion.biblioteca.domain.Prestamo;
import com.gestion.biblioteca.domain.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultaResponseDTO {

    private LocalDate fechaGeneracion;
    private LocalDate fechaPago;
    private String descripcion;
    private boolean pagada;
    private BigDecimal monto;
    private Prestamo prestamo;
    private Usuario usuario;
}
