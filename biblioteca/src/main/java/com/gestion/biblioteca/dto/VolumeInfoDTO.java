package com.gestion.biblioteca.dto;

import com.gestion.biblioteca.domain.Autor;
import lombok.Data;

import java.util.List;

@Data
    public class VolumeInfoDTO {

    private String title;
    private List<String> authors;
    private String publishedDate;

    public String getFirstAuthor() {
        return authors != null && !authors.isEmpty() ? authors.get(0) : "Autor desconocido";
    }
}
