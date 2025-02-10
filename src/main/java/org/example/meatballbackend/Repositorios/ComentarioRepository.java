package org.example.meatballbackend.Repositorios;

import org.example.meatballbackend.Entidades.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {

    // encontrar una lista de comentarios por publicacion
    List<Comentario> findByPublicacionId(int publicacionId);
}
