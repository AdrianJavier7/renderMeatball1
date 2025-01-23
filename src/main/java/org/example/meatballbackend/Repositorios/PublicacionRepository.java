package org.example.meatballbackend.Repositorios;

import org.example.meatballbackend.Entidades.Publicacion;
import org.example.meatballbackend.Entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Integer> {
    List<Publicacion> findByUsuarioId(Integer idUsuario);

    List<Publicacion> findByUsuario(Usuario usuario);

    @Query("SELECT p FROM Publicacion p WHERE p.usuario <> :usuario")
    List<Publicacion> findAllExceptByUsuario(@Param("usuario") Usuario usuario);
}
