package com.gestion.biblioteca.service;

import com.gestion.biblioteca.domain.Rol;
import com.gestion.biblioteca.domain.Usuario;
import com.gestion.biblioteca.dto.UsuarioDTO;
import com.gestion.biblioteca.dto.UsuarioResponseDTO;
import com.gestion.biblioteca.exception.ApiException;
import com.gestion.biblioteca.exception.MensajeError;
import com.gestion.biblioteca.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class UsuarioService {

    public final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    public UsuarioResponseDTO crearUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.findByDni(usuarioDTO.getDni()).isPresent()) {
            throw new ApiException(MensajeError.USUARIO_EXISTING);
        }

        String hashedPassword = passwordEncoder.encode(usuarioDTO.getPassWord());
        Usuario usuario = Usuario.builder()
                .passWord(hashedPassword)
                .dni(usuarioDTO.getDni())
                .mail(usuarioDTO.getMail())
                .nombreCompleto(usuarioDTO.getNombreCompleto())
                .rol(Rol.USER)
                .build();
        usuarioRepository.save(usuario);

        return modelMapper.map(usuario, UsuarioResponseDTO.class);

    }

    public UsuarioResponseDTO asignarRol(String dni, Rol nuevoRol) {
        Usuario usuario = usuarioRepository.findByDni(dni)
                .orElseThrow(() -> new ApiException(MensajeError.USUARIO_NOT_FOUND));
        usuario.setRol(nuevoRol);
        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return modelMapper.map(updatedUsuario, UsuarioResponseDTO.class);

    }

    public void deleteUsuario(Long id){
        if (!usuarioRepository.existsById(id)) {
            throw new ApiException(MensajeError.USUARIO_NOT_FOUND);
        }
        usuarioRepository.deleteById(id);
    }

}
