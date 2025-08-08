package com.gestion.biblioteca.service;

import com.gestion.biblioteca.domain.Autor;
import com.gestion.biblioteca.dto.AutorResponseDTO;
import com.gestion.biblioteca.exception.ApiException;
import com.gestion.biblioteca.exception.MensajeError;
import com.gestion.biblioteca.repository.AutorRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;
    private final ModelMapper modelMapper;

    public AutorResponseDTO crearAutor(String nombreCompleto){
        if(autorRepository.findByNombreCompleto(nombreCompleto).isPresent()){
            throw new ApiException(MensajeError.AUTOR_EXISTING);
        }
        Autor autor = Autor.builder()
                .nombreCompleto(nombreCompleto)
                .build();
        Autor autorGUardado = autorRepository.save(autor);
        return modelMapper.map(autorGUardado, AutorResponseDTO.class);

    }

    public Autor obtenerAutor(String nombreAutor) {
        return autorRepository.findByNombreCompleto(nombreAutor)
                .orElseGet(() -> {
                    Autor nuevoAutor = new Autor();
                    nuevoAutor.setNombreCompleto(nombreAutor);
                    return autorRepository.save(nuevoAutor);
                });
    }

    public List<AutorResponseDTO> buscarAutoresFiltro(String filtro, Pageable pageable) {
        Page<Autor> autoresEncontrados = autorRepository.findByNombreCompletoContainingIgnoreCase(filtro, pageable);
        return autoresEncontrados.stream()
                .map(autor -> modelMapper.map(autor, AutorResponseDTO.class))
                .collect(Collectors.toList());
    }

    public void deleteAutor(Long id){
        if(!autorRepository.existsById(id)){
            throw new ApiException(MensajeError.USUARIO_NOT_FOUND);
        }
        autorRepository.deleteById(id);
    }
}
