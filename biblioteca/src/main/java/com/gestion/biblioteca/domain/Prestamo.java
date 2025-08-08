package com.gestion.biblioteca.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate fechaPrestamo;
    private LocalDate fechaLimite;
    @Builder.Default
    private LocalDate fechaRealDevolucion = null;
    @Builder.Default
    private boolean devuelto = false;
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;


    public boolean debePagarMulta() {
        return this.fechaRealDevolucion.isAfter(this.fechaLimite);
    }

    public long calcularDiasRetraso(LocalDate fechaRealDevolucion, LocalDate fechaLimite ){
        return ChronoUnit.DAYS.between(fechaLimite, fechaRealDevolucion );
    }
}
