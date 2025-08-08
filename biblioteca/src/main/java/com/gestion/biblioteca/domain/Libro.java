package com.gestion.biblioteca.domain;

import com.gestion.biblioteca.dto.VolumeInfoDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String isbn;
    private String anioPublicacion;
    private int cantidadDisponible;
    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;

    public static Libro create(VolumeInfoDTO volumeInfoDTO, String isbn, Autor autor, int cantidad) {
        return Libro.builder()
                .titulo(volumeInfoDTO.getTitle())
                .isbn(isbn)
                .anioPublicacion((volumeInfoDTO.getPublishedDate()))
                .autor(autor)
                .cantidadDisponible(cantidad)
                .build();
    }

    public void descontarCantidad(){
        cantidadDisponible -= 1;
    }

    public void aumentarCantidad() {
        cantidadDisponible += 1;
    }

    public boolean isAvailable() {
        return this.cantidadDisponible > 0;
    }
}
