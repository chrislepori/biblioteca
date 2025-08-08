package com.gestion.biblioteca.repository;

import com.gestion.biblioteca.domain.Autor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

   Optional<Autor> findByNombreCompleto(String nombreCompleto);
   Page<Autor> findByNombreCompletoContainingIgnoreCase(String filtro, Pageable pageable);


}
