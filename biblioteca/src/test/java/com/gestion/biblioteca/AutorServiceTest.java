package com.gestion.biblioteca;

import com.gestion.biblioteca.domain.Autor;
import com.gestion.biblioteca.dto.AutorResponseDTO;
import com.gestion.biblioteca.repository.AutorRepository;
import com.gestion.biblioteca.service.AutorService;
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
public class AutorServiceTest {

    @InjectMocks
    private AutorService autorService;
    @Mock
    private AutorRepository autorRepository;
    @Mock
    private ModelMapper modelMapper;
    @Test
    @DisplayName("Debe crear un autor exitosamente cuando el nombre no existe")
    void crearAutorSuccess() {
        String nombreAutor = "Julio Doe";

        //Simulamos que no encuentra al autor
        when(autorRepository.findByNombreCompleto(nombreAutor)).thenReturn(Optional.empty());

        //Simulamos el comportamiento del repo para guardar
        Autor autorParaGuardar = Autor.builder().nombreCompleto(nombreAutor).build();
        Autor autorGuardadoConId = Autor.builder().id(1L).nombreCompleto(nombreAutor).build();
        when(autorRepository.save(any(Autor.class))).thenReturn(autorGuardadoConId);

        //Simulamos el modelmapper para devolver el AutorResponseDTO
        AutorResponseDTO expectedResponseDTO = new AutorResponseDTO();
        expectedResponseDTO.setNombreCompleto(nombreAutor);
        when(modelMapper.map(autorGuardadoConId, AutorResponseDTO.class)).thenReturn(expectedResponseDTO);

        //Se ejecuta el metodo que estamos probando
        AutorResponseDTO resultado = autorService.crearAutor(nombreAutor);

        //Se verifican los resultados y las interacciones
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(nombreAutor, resultado.getNombreCompleto(), "El nombre del autor debe coincidir");

        //Verificamos que autorRepository.findByNombreCompleto() fue llamado una vez
        verify(autorRepository, times(1)).findByNombreCompleto(nombreAutor);

        //Verificamos que autorRepository.save() fue llamado una vez
        verify(autorRepository, times(1)).save(argThat(autor ->
                autor.getNombreCompleto().equals(nombreAutor) && autor.getId() == null
        ));

        // Verificamos que modelMapper.map() fue llamado exactamente una vez con los argumentos correctos
        verify(modelMapper, times(1)).map(autorGuardadoConId, AutorResponseDTO.class);
    }



}
