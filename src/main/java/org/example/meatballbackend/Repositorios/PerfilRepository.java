package org.example.meatballbackend.Repositorios;

import org.example.meatballbackend.Entidades.Perfil;
import org.example.meatballbackend.Entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {
    Perfil findTopByUsuario(Usuario usuario);
}
