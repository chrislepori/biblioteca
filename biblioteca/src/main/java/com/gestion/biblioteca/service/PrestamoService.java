package com.gestion.biblioteca.service;

import com.gestion.biblioteca.domain.Libro;
import com.gestion.biblioteca.domain.Prestamo;
import com.gestion.biblioteca.domain.Usuario;
import com.gestion.biblioteca.dto.PrestamoDTO;
import com.gestion.biblioteca.dto.PrestamoResponseDTO;
import com.gestion.biblioteca.exception.ApiException;
import com.gestion.biblioteca.exception.MensajeError;
import com.gestion.biblioteca.repository.LibroRepository;
import com.gestion.biblioteca.repository.MultaRepository;
import com.gestion.biblioteca.repository.PrestamoRepository;
import com.gestion.biblioteca.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PrestamoService {

    private static final int CANT_DIAS = 10;

    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;
    private final MultaService multaService;


    public PrestamoResponseDTO crearPrestamo(PrestamoDTO prestamoDTO){
        Libro libro = existeYHayStock(prestamoDTO);
        Usuario usuario = existeUsuario(prestamoDTO);
        validarEstadoDeudor(usuario);

        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaLimite = fechaActual.plusDays(CANT_DIAS);

        Prestamo prestamo = Prestamo.builder()
                .libro(libro)
                .usuario(usuario)
                .fechaPrestamo(fechaActual)
                .fechaLimite(fechaLimite)
                .devuelto(false)
                .fechaRealDevolucion(null)
                .build();

        libro.descontarCantidad();
        prestamoRepository.save(prestamo);

        return modelMapper.map(prestamo, PrestamoResponseDTO.class);
    }

    public void devolverLibro(Long prestamoId){
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new ApiException(MensajeError.PRESTAMO_NOT_FOUND));

        if(prestamo.isDevuelto()){
            throw new ApiException(MensajeError.LIBRO_YA_DEVUELTO);
        }
        prestamo.setDevuelto(true);
        prestamo.setFechaRealDevolucion(LocalDate.now());

        Libro libro = prestamo.getLibro();
        libro.aumentarCantidad();
        libroRepository.save(libro);

        if(prestamo.debePagarMulta()){
            String descripcion = "Multa por libro " + prestamo.getLibro().getTitulo();
            long cantidadDiasRetraso = prestamo.calcularDiasRetraso(prestamo.getFechaLimite(), prestamo.getFechaRealDevolucion());
            multaService.crearMulta(prestamo, prestamo.getUsuario() ,cantidadDiasRetraso, descripcion);
        }
        prestamoRepository.save(prestamo);
    }

    private Usuario existeUsuario(PrestamoDTO prestamoDTO) {
        return usuarioRepository.findById(prestamoDTO.getUsuarioId())
                .orElseThrow(()-> new ApiException(MensajeError.USUARIO_NOT_FOUND));
    }

    private Libro existeYHayStock(PrestamoDTO prestamoDTO) {
        Libro libro = libroRepository.findById(prestamoDTO.getLibroId())
                .orElseThrow(() -> new ApiException(MensajeError.LIBRO_NOT_FOUND));
        if(!libro.isAvailable()){
            throw new ApiException(MensajeError.LIBRO_NOT_AVAILABLE);

        }
        return libro;
    }

    private void validarEstadoDeudor(Usuario usuario) {
        boolean tienePrestamosVencidos = prestamoRepository.existsByUsuarioAndFechaLimiteBeforeAndDevueltoIsFalse(usuario, LocalDate.now());
        boolean tieneMultasPendientes = multaService.existsMultasPendientesByUsuario(usuario);
        if (tienePrestamosVencidos || tieneMultasPendientes) {
            throw new ApiException(MensajeError.USUARIO_ES_DEUDOR);
        }
    }

    public void deletePrestamo(Long id){
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new ApiException(MensajeError.PRESTAMO_NOT_FOUND));
        Libro libro = prestamo.getLibro();
        prestamoRepository.delete(prestamo);
        libro.aumentarCantidad();


    }

    public PrestamoResponseDTO findById(Long id){
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new ApiException(MensajeError.PRESTAMO_NOT_FOUND));
        return modelMapper.map(prestamo, PrestamoResponseDTO.class);

    }










}
