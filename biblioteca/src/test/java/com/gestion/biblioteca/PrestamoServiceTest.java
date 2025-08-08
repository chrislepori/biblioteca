package com.gestion.biblioteca;
import com.gestion.biblioteca.domain.Libro;
import com.gestion.biblioteca.domain.Prestamo;
import com.gestion.biblioteca.domain.Usuario;
import com.gestion.biblioteca.exception.ApiException;
import com.gestion.biblioteca.exception.MensajeError;
import com.gestion.biblioteca.repository.LibroRepository;
import com.gestion.biblioteca.repository.PrestamoRepository;
import com.gestion.biblioteca.service.MultaService;
import com.gestion.biblioteca.service.PrestamoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PrestamoServiceTest {

    @InjectMocks
    private PrestamoService prestamoService;

    @Mock
    private PrestamoRepository prestamoRepository;

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private MultaService multaService;

    @Test
    @DisplayName("Debe devolver el libro y actualizar el prestamo cuando no hay multa")
    void devolverLibro_sinMulta_debeActualizarYGuardar() {

        Long prestamoId = 1L;
        Libro libro = Libro.builder().id(10L).cantidadDisponible(5).build();

        //Prestamo es un MOCK ahora
        Prestamo prestamo = mock(Prestamo.class);
        when(prestamo.isDevuelto()).thenReturn(false);
        when(prestamo.getLibro()).thenReturn(libro);
        when(prestamo.debePagarMulta()).thenReturn(false);

        when(prestamoRepository.findById(prestamoId)).thenReturn(Optional.of(prestamo));

        prestamoService.devolverLibro(prestamoId);

        //Verificamos que se actualizó la cantidad del libro
        assertEquals(6, libro.getCantidadDisponible(), "La cantidad del libro debe aumentar en 1");

        //Verificamos las interacciones con el mock
        verify(prestamo, times(1)).setDevuelto(true);
        verify(prestamo, times(1)).setFechaRealDevolucion(any(LocalDate.class));

        verify(prestamoRepository, times(1)).findById(prestamoId);
        verify(libroRepository, times(1)).save(libro);
        verify(prestamoRepository, times(1)).save(prestamo);
        verify(multaService, never()).crearMulta(any(), any(), anyLong(), anyString());
    }


    @Test
    @DisplayName("Debe devolver el libro, actualizar el prestamo y crear una multa cuando hay retraso")
    void devolverLibro_conMulta_debeCrearMultaYActualizarYGuardar() {

        Long prestamoId = 2L;
        Usuario usuario = Usuario.builder().id(1L).nombreCompleto("John Doe").build();
        Libro libro = Libro.builder().id(20L).cantidadDisponible(2).titulo("Libro A").build();
        long diasRetraso = 3L;

        //Prestamo es un MOCK ahora
        Prestamo prestamo = mock(Prestamo.class);
        when(prestamo.isDevuelto()).thenReturn(false);
        when(prestamo.getLibro()).thenReturn(libro);
        when(prestamo.getUsuario()).thenReturn(usuario);
        when(prestamo.debePagarMulta()).thenReturn(true);
        when(prestamo.calcularDiasRetraso(any(), any())).thenReturn(diasRetraso);

        when(prestamoRepository.findById(prestamoId)).thenReturn(Optional.of(prestamo));

        prestamoService.devolverLibro(prestamoId);

        assertEquals(3, libro.getCantidadDisponible(), "La cantidad del libro debe aumentar en 1");

        //Verificamos las interacciones con el mock
        verify(prestamo, times(1)).setDevuelto(true);
        verify(prestamo, times(1)).setFechaRealDevolucion(any(LocalDate.class));

        verify(prestamoRepository, times(1)).findById(prestamoId);
        verify(libroRepository, times(1)).save(libro);
        verify(prestamoRepository, times(1)).save(prestamo);

        verify(multaService, times(1)).crearMulta(
                eq(prestamo),
                eq(usuario),
                eq(diasRetraso),
                eq("Multa por libro " + libro.getTitulo()));
    }


    @Test
    @DisplayName("Debe lanzar una excepcion cuando el prestamo no existe")
    void devolverLibro_prestamoNoExiste_debeLanzarExcepcion() {

        Long prestamoId = 99L;
        when(prestamoRepository.findById(prestamoId)).thenReturn(Optional.empty());

        ApiException thrown = assertThrows(
                ApiException.class,
                () -> prestamoService.devolverLibro(prestamoId),
                "Se esperaba que lanzara una excepción de prestamo no encontrado"
        );
        assertEquals(MensajeError.PRESTAMO_NOT_FOUND, thrown.getError());

        verify(prestamoRepository, times(1)).findById(prestamoId);
        verify(libroRepository, never()).save(any());
        verify(prestamoRepository, never()).save(any());
        verify(multaService, never()).crearMulta(any(), any(), anyLong(), anyString());
    }



    @Test
    @DisplayName("Debe lanzar una excepcion cuando el libro ya ha sido devuelto")
    void devolverLibro_yaDevuelto_debeLanzarExcepcion() {

        Long prestamoId = 3L;
        Prestamo prestamoYaDevuelto = mock(Prestamo.class);
        when(prestamoYaDevuelto.isDevuelto()).thenReturn(true);

        when(prestamoRepository.findById(prestamoId)).thenReturn(Optional.of(prestamoYaDevuelto));

        ApiException thrown = assertThrows(
                ApiException.class,
                () -> prestamoService.devolverLibro(prestamoId),
                "Se esperaba que lanzara una excepción de libro ya devuelto"
        );
        assertEquals(MensajeError.LIBRO_YA_DEVUELTO, thrown.getError());

        verify(prestamoRepository, times(1)).findById(prestamoId);
        verify(libroRepository, never()).save(any());
        verify(prestamoRepository, never()).save(any());
        verify(multaService, never()).crearMulta(any(), any(), anyLong(), anyString());
    }


}
