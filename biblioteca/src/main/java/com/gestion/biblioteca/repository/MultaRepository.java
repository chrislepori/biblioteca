package com.gestion.biblioteca.repository;

import com.gestion.biblioteca.domain.Multa;
import com.gestion.biblioteca.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MultaRepository extends JpaRepository<Multa, Long> {

    Page<Multa> findAll(Pageable pageable);
    Page<Multa> findByUsuario(Usuario usuario, Pageable pageable);
    Page<Multa> findByPagadaFalse(Pageable pageable);
    Page<Multa> findByUsuarioAndPagadaFalse(Usuario usuario, Pageable pageable);
    boolean existsByUsuarioAndPagadaIsFalse(Usuario usuario);

}
