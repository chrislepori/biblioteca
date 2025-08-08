package com.gestion.biblioteca.controller;

import com.gestion.biblioteca.dto.LibroDTO;
import com.gestion.biblioteca.dto.LibroResponseDTO;
import com.gestion.biblioteca.service.LibroService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/libros")
public class LibroController {

    private final LibroService libroService;

    @PostMapping
    public ResponseEntity<LibroResponseDTO> crearLibro(@Valid @RequestBody LibroDTO crearLibroDTO){
        return ResponseEntity.ok().body(libroService.crearLibro(crearLibroDTO));
    }

    @GetMapping("/buscarPorNombre")
    public ResponseEntity<Page<LibroResponseDTO>> buscarLibroFitrado(@RequestParam String filter, Pageable pageable){
        return ResponseEntity.ok(libroService.buscarLibrosFilter(filter, pageable));
    }

    @GetMapping("/buscarPorAutor")
    public ResponseEntity<Page<LibroResponseDTO>> obtenerLibrosAutor(@RequestParam String nombreCompleto, Pageable pageable){
        return ResponseEntity.ok(libroService.obtenerLibrosAutor(nombreCompleto, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(libroService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibro(@PathVariable Long id) {
        libroService.deleteLibro(id);
        return ResponseEntity.noContent().build();
    }
}
