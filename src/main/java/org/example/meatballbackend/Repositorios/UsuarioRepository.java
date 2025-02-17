package org.example.meatballbackend.Repositorios;

import org.example.meatballbackend.Entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findTopByUsername(String username);

    @Query("SELECT COALESCE(COUNT(u), 0) FROM Usuario u JOIN u.seguidos s WHERE s.id = :id")
    int contarTotalSeguidores(@Param("id") int id);

}