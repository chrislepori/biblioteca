package com.gestion.biblioteca.controller;

import com.gestion.biblioteca.domain.Rol;
import com.gestion.biblioteca.dto.UsuarioDTO;
import com.gestion.biblioteca.dto.UsuarioResponseDTO;
import com.gestion.biblioteca.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO){
        return ResponseEntity.ok(usuarioService.crearUsuario(usuarioDTO));

    }

    @PutMapping("/{dni}")
    public ResponseEntity<UsuarioResponseDTO> asignarNuevoRol(@PathVariable String dni, @RequestBody Rol nuevoRol){
        return ResponseEntity.ok(usuarioService.asignarRol(dni, nuevoRol));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }


}
