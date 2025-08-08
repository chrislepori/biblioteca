package com.gestion.biblioteca;
import com.gestion.biblioteca.domain.Rol;
import com.gestion.biblioteca.domain.Usuario;
import com.gestion.biblioteca.dto.UsuarioDTO;
import com.gestion.biblioteca.dto.UsuarioResponseDTO;
import com.gestion.biblioteca.exception.ApiException;
import com.gestion.biblioteca.exception.MensajeError;
import com.gestion.biblioteca.repository.UsuarioRepository;
import com.gestion.biblioteca.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.argThat;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @Test
    @DisplayName("Debe crear un usuario exitosamente cuando el DNI no existe")
    void crearUsuario_dniNoExiste_debeCrearUsuario() {

        UsuarioDTO usuarioDTO = new UsuarioDTO("123456", "Pedro Perez", "pperez@mail.com", "password123", Rol.USER);
        String hashedPassword = "hashedPassword";

        when(usuarioRepository.findByDni(usuarioDTO.getDni())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(usuarioDTO.getPassWord())).thenReturn(hashedPassword);

        Usuario usuarioGuardado = Usuario.builder()
                .id(1L)
                .passWord(hashedPassword)
                .dni(usuarioDTO.getDni())
                .mail(usuarioDTO.getMail())
                .nombreCompleto(usuarioDTO.getNombreCompleto())
                .rol(Rol.USER)
                .build();
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        UsuarioResponseDTO expectedResponseDTO = new UsuarioResponseDTO();
        expectedResponseDTO.setDni(usuarioDTO.getDni());
        expectedResponseDTO.setNombreCompleto(usuarioDTO.getNombreCompleto());
        expectedResponseDTO.setMail(usuarioDTO.getMail());
        when(modelMapper.map(any(Usuario.class), eq(UsuarioResponseDTO.class))).thenReturn(expectedResponseDTO);

        UsuarioResponseDTO resultado = usuarioService.crearUsuario(usuarioDTO);

        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(expectedResponseDTO.getDni(), resultado.getDni(), "El DNI debe coincidir");
        assertEquals(expectedResponseDTO.getNombreCompleto(), resultado.getNombreCompleto(), "El nombre debe coincidir");
        assertEquals(expectedResponseDTO.getMail(), resultado.getMail(), "El mail debe coincidir");

        verify(usuarioRepository, times(1)).findByDni(usuarioDTO.getDni());
        verify(passwordEncoder, times(1)).encode(usuarioDTO.getPassWord());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(modelMapper, times(1)).map(any(Usuario.class), eq(UsuarioResponseDTO.class));
    }


    @Test
    @DisplayName("Debe lanzar una excepcion cuando se intenta crear un usuario con un DNI existente")
    void crearUsuario_dniExistente_debeLanzarExcepcion() {

        UsuarioDTO usuarioDTO = new UsuarioDTO("123456", "Pedro Perez", "pperez@mail.com", "password123", Rol.USER);

        when(usuarioRepository.findByDni(usuarioDTO.getDni())).thenReturn(Optional.of(new Usuario()));

        ApiException thrown = assertThrows(
                ApiException.class,
                () -> usuarioService.crearUsuario(usuarioDTO),
                "Se esperaba que el método lanzara una ApiException"
        );

        assertEquals(MensajeError.USUARIO_EXISTING, thrown.getError(), "El mensaje de error no coincide");

        verify(usuarioRepository, times(1)).findByDni(usuarioDTO.getDni());
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(modelMapper, never()).map(any(), any());
    }
}

