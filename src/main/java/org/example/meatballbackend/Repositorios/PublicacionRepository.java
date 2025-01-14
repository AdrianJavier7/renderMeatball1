package org.example.meatballbackend.Repositorios;

import org.example.meatballbackend.Entidades.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Integer> {


}
