package com.gestion.biblioteca.controller;

import com.gestion.biblioteca.dto.AutorResponseDTO;
import com.gestion.biblioteca.service.AutorService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/autores")
public class AutorController {

    private final AutorService autorService;

    @GetMapping("/{filter}")
    public ResponseEntity<List<AutorResponseDTO>> buscarAutorFiltrado(@PathVariable String filter, Pageable pageable){
        return ResponseEntity.ok(autorService.buscarAutoresFiltro(filter, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAutor(@PathVariable Long id){
        autorService.deleteAutor(id);
        return ResponseEntity.noContent().build();
    }
}
