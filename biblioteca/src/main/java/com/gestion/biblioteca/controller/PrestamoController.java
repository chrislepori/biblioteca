package com.gestion.biblioteca.controller;

import com.gestion.biblioteca.dto.PrestamoDTO;
import com.gestion.biblioteca.dto.PrestamoResponseDTO;
import com.gestion.biblioteca.service.PrestamoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    @PostMapping
    public ResponseEntity<PrestamoResponseDTO> crearPrestamo(@Valid @RequestBody PrestamoDTO prestamoDTO){
        return ResponseEntity.ok(prestamoService.crearPrestamo(prestamoDTO));
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<Void> devolverLibro(@PathVariable Long id){
        prestamoService.devolverLibro(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(prestamoService.findById(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrestamo(@PathVariable Long id){
        prestamoService.deletePrestamo(id);
        return ResponseEntity.noContent().build();
    }

}
