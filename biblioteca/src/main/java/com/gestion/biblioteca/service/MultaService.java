package com.gestion.biblioteca.service;

import com.gestion.biblioteca.domain.Multa;
import com.gestion.biblioteca.domain.Prestamo;
import com.gestion.biblioteca.domain.Usuario;
import com.gestion.biblioteca.dto.MultaResponseDTO;
import com.gestion.biblioteca.exception.ApiException;
import com.gestion.biblioteca.exception.MensajeError;
import com.gestion.biblioteca.repository.MultaRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class MultaService {

    private static final BigDecimal COSTO_MULTA = BigDecimal.valueOf(1000);

    private final MultaRepository multaRepository;
    private ModelMapper modelMapper;

    public MultaResponseDTO crearMulta(Prestamo prestamo, Usuario usuario, long diasRetraso, String descripcion) {
        if (diasRetraso <= 0) {
            throw new ApiException(MensajeError.DIAS_ICORRECTOS);
        }

        BigDecimal montoTotalMulta = COSTO_MULTA.multiply(BigDecimal.valueOf(diasRetraso));

        Multa multa = Multa.builder()
                .prestamo(prestamo)
                .usuario(usuario)
                .monto(montoTotalMulta)
                .fechaGeneracion(LocalDate.now())
                .descripcion(descripcion)
                .pagada(false)
                .build();
        multaRepository.save(multa);
        return modelMapper.map(multa, MultaResponseDTO.class);
    }

    public MultaResponseDTO pagarMulta(Long id){
        Multa multa = multaRepository.findById(id)
                .orElseThrow(() -> new ApiException(MensajeError.MULTA_NOT_FOUND));

        if(multa.isPagada()){
            throw new ApiException(MensajeError.MULTA_ALREADY_PAID);
        }

        multa.setPagada(true);
        multa.setFechaPago(LocalDate.now());
        multaRepository.save(multa);
        return modelMapper.map(multa, MultaResponseDTO.class);

    }


    public boolean existsMultasPendientesByUsuario(Usuario usuario) {
        return multaRepository.existsByUsuarioAndPagadaIsFalse(usuario);
    }

    public Page<MultaResponseDTO> getMultas(Pageable pageable){
        Page<Multa> multas = multaRepository.findAll(pageable);
        return multas.map(multa -> modelMapper.map(multa, MultaResponseDTO.class));
    }

    public Page<MultaResponseDTO> getUsuarioMultas(Usuario usuario, Pageable pageable){
        Page<Multa> usuarioMultas = multaRepository.findByUsuario(usuario, pageable);
        return usuarioMultas.map(multa -> modelMapper.map(multa, MultaResponseDTO.class));
    }

    public Page<MultaResponseDTO> getMultasSinPagar(Pageable pageable){
        Page<Multa> multasImpagas = multaRepository.findByPagadaFalse(pageable);
        return multasImpagas.map(multa -> modelMapper.map(multa, MultaResponseDTO.class));
    }

    public Page<MultaResponseDTO> getDeudor(Usuario usuario, Pageable pageable){
        Page<Multa> multasImpagasUsuario = multaRepository.findByUsuarioAndPagadaFalse(usuario, pageable);
        return multasImpagasUsuario.map(multa -> modelMapper.map(multa, MultaResponseDTO.class));
    }





}
