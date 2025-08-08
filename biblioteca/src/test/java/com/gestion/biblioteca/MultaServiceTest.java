package com.gestion.biblioteca;
import com.gestion.biblioteca.domain.Multa;
import com.gestion.biblioteca.domain.Prestamo;
import com.gestion.biblioteca.domain.Usuario;
import com.gestion.biblioteca.dto.MultaResponseDTO;
import com.gestion.biblioteca.repository.MultaRepository;
import com.gestion.biblioteca.service.MultaService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.argThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MultaServiceTest {

    @InjectMocks
    private MultaService multaService;
    @Mock
    private MultaRepository multaRepository;
    @Mock
    private ModelMapper modelMapper;

    @Test
    @DisplayName("Debe devolver una página de multas cuando el usuario tiene multas")
    void getUsuarioMultas_conMultas_debeDevolverPaginaDeMultas(){

        //preparado de escenario
        Usuario usuario = Usuario.builder().id(1L).nombreCompleto("John Doe").build();
        Prestamo prestamo = Prestamo.builder().id(100L).build();
        Pageable pageable = PageRequest.of(0, 10);

        //Simulación de lista de multas
        Multa multa1 = Multa.builder()
                .id(1L)
                .monto(new BigDecimal("100.00"))
                .usuario(usuario)
                .prestamo(prestamo)
                .fechaGeneracion(LocalDate.now())
                .descripcion("Multa por retraso")
                .pagada(false)
                .build();

        List<Multa> multasDelUsuario = List.of(multa1);

        //Creación de página que simula lo que devolvería el repo
        Page<Multa> paginaDeMultas = new PageImpl<>(multasDelUsuario, pageable, multasDelUsuario.size());

        //Simulación del dto
        MultaResponseDTO dto1 = new MultaResponseDTO();
        dto1.setMonto(new BigDecimal("100.00"));
        dto1.setUsuario(usuario);
        dto1.setPrestamo(prestamo);
        dto1.setFechaGeneracion(LocalDate.now());
        dto1.setDescripcion("Multa por retraso");
        dto1.setPagada(false);


        //Configuración del mock para el repositorio
        when(multaRepository.findByUsuario(usuario, pageable)).thenReturn(paginaDeMultas);

        //Configuración del mock para el mapper
        when(modelMapper.map(multa1, MultaResponseDTO.class)).thenReturn(dto1);

        //Prueba
        Page<MultaResponseDTO> resultado = multaService.getUsuarioMultas(usuario, pageable);

        //Verificar
        assertNotNull(resultado, "La página de resultados no debería ser nula");
        assertEquals(1, resultado.getContent().size(), "El tamaño de la lista debe ser 1");

        MultaResponseDTO multaResultado = resultado.getContent().get(0);

        //Aserciones sobre las propiedades del DTO
        assertEquals(dto1.getMonto(), multaResultado.getMonto(), "El monto debe coincidir");
        assertEquals(dto1.getDescripcion(), multaResultado.getDescripcion(), "La descripción debe coincidir");
        assertEquals(dto1.getFechaGeneracion(), multaResultado.getFechaGeneracion(), "La fecha de generación debe coincidir");
        assertEquals(dto1.isPagada(), multaResultado.isPagada(), "El estado de pago debe coincidir");

        //Aserciones sobre los objetos anidados
        assertNotNull(multaResultado.getUsuario(), "El usuario no debe ser nulo");
        assertEquals(dto1.getUsuario().getNombreCompleto(), multaResultado.getUsuario().getNombreCompleto(), "El nombre del usuario debe coincidir");

        assertNotNull(multaResultado.getPrestamo(), "El prestamo no debe ser nulo");
        assertEquals(dto1.getPrestamo().getId(), multaResultado.getPrestamo().getId(), "El ID del prestamo debe coincidir");


        //Verificaciones de las llamadas a los mocks
        verify(multaRepository, times(1)).findByUsuario(usuario, pageable);
        verify(modelMapper, times(1)).map(multa1, MultaResponseDTO.class);

    }


}

