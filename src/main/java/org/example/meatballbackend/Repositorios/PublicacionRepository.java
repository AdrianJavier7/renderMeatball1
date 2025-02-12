package org.example.meatballbackend.Repositorios;

import org.example.meatballbackend.Entidades.Perfil;
import org.example.meatballbackend.Entidades.Publicacion;
import org.example.meatballbackend.Entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Integer> {
    List<Publicacion> findByUsuarioId(Integer idUsuario);

    // quiero obtener una publicacion por id de la publicacion
    Optional<Publicacion> findById(Integer id);

    List<Publicacion> findByUsuario(Usuario usuario);

    @Query("SELECT p FROM Publicacion p WHERE p.usuario <> :usuario")
    List<Publicacion> findAllExceptByUsuario(@Param("usuario") Usuario usuario);

    List<Publicacion> findAll();

    Publicacion findTopByUsuario(Usuario usuario);

    List<Publicacion> findByUsuarioIdIn(List<Integer> usuarioIds);


}
