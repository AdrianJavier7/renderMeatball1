package org.example.meatballbackend.Servicios;

import org.example.meatballbackend.Entidades.Perfil;
import org.example.meatballbackend.Entidades.Usuario;
import org.example.meatballbackend.Repositorios.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerfilService implements IPerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    public Perfil buscarPorUsuario(Usuario usuario){
        return perfilRepository.findTopByUsuario(usuario);
    }

    public Perfil guardarPerfil(Perfil perfil){
        return perfilRepository.save(perfil);
    }
}
