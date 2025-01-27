package org.example.meatballbackend.Servicios;

import org.example.meatballbackend.Dto.PerfilDTO;
import org.example.meatballbackend.Entidades.Perfil;
import org.example.meatballbackend.Entidades.Usuario;
import org.example.meatballbackend.Repositorios.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<PerfilDTO> getAll(){
        List<Perfil> perfiles = perfilRepository.findAll();
        List<PerfilDTO> perfilDTOS = new ArrayList<>();

        for(Perfil p : perfiles){
            PerfilDTO dto = new PerfilDTO();
            dto.setUsername(p.getUsername());
            dto.setEmail(p.getEmail());
            perfilDTOS.add(dto);
        }

        return perfilDTOS;
    }
}
