package com.gestion.biblioteca.service;

import com.gestion.biblioteca.client.GoogleBooksClient;
import com.gestion.biblioteca.domain.Autor;
import com.gestion.biblioteca.domain.Libro;
import com.gestion.biblioteca.dto.GoogleBooksApiResponseDTO;
import com.gestion.biblioteca.dto.LibroDTO;
import com.gestion.biblioteca.dto.LibroResponseDTO;
import com.gestion.biblioteca.dto.VolumeInfoDTO;
import com.gestion.biblioteca.exception.ApiException;
import com.gestion.biblioteca.exception.MensajeError;
import com.gestion.biblioteca.repository.LibroRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LibroService {


    private final LibroRepository libroRepository;
    private final AutorService autorService;
    private final GoogleBooksClient googleBooksClient;
    private final ModelMapper modelMapper;

    public LibroResponseDTO crearLibro(LibroDTO libroDTO) {
        if (libroRepository.findByIsbn(libroDTO.getIsbn()).isPresent()) {
            throw new ApiException(MensajeError.LIBRO_EXISTING);
        }

        VolumeInfoDTO volumeInfo = getBookInfoFromGoogle(libroDTO);

        String nombreAutor = volumeInfo.getFirstAuthor();

        Autor autor = autorService.obtenerAutor(nombreAutor);


        Libro libro = Libro.create(volumeInfo, libroDTO.getIsbn(), autor, libroDTO.getCantidadInicial());
        libroRepository.save(libro);

        return modelMapper.map(libro, LibroResponseDTO.class);

    }

    private VolumeInfoDTO getBookInfoFromGoogle(LibroDTO libroDTO) {
        GoogleBooksApiResponseDTO response = googleBooksClient.buscarPorIsbn("isbn:" + libroDTO.getIsbn());

        if (response == null || response.getItems() == null || response.getItems().isEmpty()) {
            throw new ApiException(MensajeError.LIBRO_NOT_FOUND_GOOGLE);
        }

        VolumeInfoDTO volumeInfo = response.getFirstVolumeInfo();
        if(volumeInfo == null){
            throw new ApiException(MensajeError.VOL_INFO);
        }
        return volumeInfo;
    }

    public Page<LibroResponseDTO> buscarLibrosFilter(String titulo, Pageable pageable){
        Page<Libro> libroEncontrado  = libroRepository.findByTituloContainingIgnoreCase(titulo, pageable);
        return libroEncontrado.map(libro -> modelMapper.map(libro, LibroResponseDTO.class));
    }

    public Page<LibroResponseDTO> obtenerLibrosAutor(String nombreCompleto, Pageable pageable){
        Page<Libro> libroEncontrado = libroRepository.findByAutorNombreCompletoContainingIgnoreCase(nombreCompleto, pageable);
        return libroEncontrado.map(libro -> modelMapper.map(libro, LibroResponseDTO.class));
    }

    public LibroResponseDTO buscarPorId(Long id){
        return libroRepository.findById(id)
                .map(libro ->modelMapper.map(libro, LibroResponseDTO.class))
                .orElseThrow(() -> new ApiException(MensajeError.LIBRO_NOT_FOUND));
    }

    public void deleteLibro(Long id){
        if (!libroRepository.existsById(id)) {
            throw new ApiException(MensajeError.LIBRO_NOT_FOUND);
        }
        libroRepository.deleteById(id);
    }



}

