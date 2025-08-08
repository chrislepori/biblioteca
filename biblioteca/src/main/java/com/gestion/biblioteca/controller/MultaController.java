package com.gestion.biblioteca.controller;

import com.gestion.biblioteca.domain.Usuario;
import com.gestion.biblioteca.dto.MultaResponseDTO;
import com.gestion.biblioteca.service.MultaService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/multas")
public class MultaController {

    private MultaService multaService;

    @GetMapping
    public ResponseEntity<Page<MultaResponseDTO>> getAllMultas(Pageable pageable){
        return ResponseEntity.ok(multaService.getMultas(pageable));
    }

    @GetMapping("/multasUsuario")
    public ResponseEntity<Page<MultaResponseDTO>> getMultasUsuario(Usuario usuario, Pageable pageable){
        return ResponseEntity.ok(multaService.getUsuarioMultas(usuario, pageable));
    }

    @GetMapping("/multasSinPagar")
    public ResponseEntity<Page<MultaResponseDTO>> getMultasSinPagar(Pageable pageable){
        return ResponseEntity.ok(multaService.getMultasSinPagar(pageable));
    }

    @GetMapping("/multasUsuarioSinPagar")
    public ResponseEntity<Page<MultaResponseDTO>> getMultasUsuarioImpagas(Usuario usuario, Pageable pageable){
        return ResponseEntity.ok(multaService.getDeudor(usuario, pageable));
    }





}
