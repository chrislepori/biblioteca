package com.gestion.biblioteca.repository;

import com.gestion.biblioteca.domain.Prestamo;
import com.gestion.biblioteca.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    Page<Prestamo> findAll(Pageable pageable);

    Page<Prestamo> findByUsuario(Usuario usuario, Pageable pageable);

    Page<Prestamo> findByDevueltoFalse(Pageable pageable);

    Page<Prestamo> findByFechaLimiteBeforeAndDevueltoFalse(LocalDate fechaActual, Pageable pageable);

    List<Prestamo> findByFechaLimiteAndDevueltoFalse(LocalDate fechaLimite);

    boolean existsByUsuarioAndFechaLimiteBeforeAndDevueltoIsFalse(Usuario usuario, LocalDate fechaActual);

}
