package org.example.meatballbackend.Servicios;

import org.example.meatballbackend.Entidades.Perfil;
import org.example.meatballbackend.Repositorios.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerfilService implements IPerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    public Perfil guardarPerfil(Perfil perfil){
        return perfilRepository.save(perfil);
    }

    public String obtenerUsername(Integer id) {
        return perfilRepository.findById(id).get().getUsuario().getUsername();
    }

    public String obtenerFotoPerfil(Integer id) {
        return perfilRepository.findById(id).get().getFotoPerfilLink();
    }
}
