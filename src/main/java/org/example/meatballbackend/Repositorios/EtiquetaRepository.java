package org.example.meatballbackend.Repositorios;

import org.example.meatballbackend.Entidades.Etiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtiquetaRepository extends JpaRepository<Etiqueta, Integer> {
    List<Etiqueta> findAll();

    Optional<Etiqueta> findByNombre(String nombre);
}
