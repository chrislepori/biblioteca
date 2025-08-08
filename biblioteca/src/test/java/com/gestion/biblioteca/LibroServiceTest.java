package com.gestion.biblioteca;
import com.gestion.biblioteca.domain.Autor;
import com.gestion.biblioteca.domain.Libro;
import com.gestion.biblioteca.dto.LibroResponseDTO;
import com.gestion.biblioteca.repository.LibroRepository;
import com.gestion.biblioteca.service.LibroService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import org.modelmapper.ModelMapper;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.argThat;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LibroServiceTest {

    @InjectMocks
    private LibroService libroService;

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    @DisplayName("Debe devolver un LibroResponseDTO cuando el libro existe")
    void buscarPorId_libroExiste_debeDevolverLibroDTO() {

        Long idLibro = 1L;
        String titulo = "Cien años de soledad";

        Libro libro = Libro.builder()
                .id(idLibro).titulo(titulo)
                .build();

        Autor autor = Autor.builder()
                .id(1L).nombreCompleto("Julio Doe")
                .build();

        LibroResponseDTO expectedDTO = new LibroResponseDTO();
        expectedDTO.setTitulo(titulo);
        expectedDTO.setAutor(autor);

        // Simulamos que el repositorio encuentra el libro y lo devuelve en un Optional
        when(libroRepository.findById(idLibro)).thenReturn(Optional.of(libro));

        // Simulamos que el ModelMapper convierte el libro en el DTO esperado
        when(modelMapper.map(libro, LibroResponseDTO.class)).thenReturn(expectedDTO);

        //Ejecutamos
        LibroResponseDTO resultado = libroService.buscarPorId(idLibro);

        //Verificamos
        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertEquals(expectedDTO.getTitulo(), resultado.getTitulo(), "El título debe coincidir");

        // Verificamos que el autor en el resultado sea el mismo
        assertNotNull(resultado.getAutor(), "El autor no debe ser nulo");
        assertEquals(expectedDTO.getAutor().getNombreCompleto(), resultado.getAutor().getNombreCompleto(), "El nombre del autor debe coincidir");
        assertEquals(expectedDTO.getAutor().getId(), resultado.getAutor().getId(), "El ID del autor debe coincidir");

        // Verificamos que el repositorio fue llamado exactamente una vez con el ID correcto
        verify(libroRepository, times(1)).findById(idLibro);

        // Verificamos que el mapeador fue llamado exactamente una vez con el objeto Libro
        verify(modelMapper, times(1)).map(libro, LibroResponseDTO.class);
    }
}

