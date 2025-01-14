package org.example.meatballbackend.Repositorios;

import org.example.meatballbackend.Entidades.Etiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtiquetaRepository extends JpaRepository<Etiqueta, Integer> {

}
