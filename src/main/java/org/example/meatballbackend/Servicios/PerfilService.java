package org.example.meatballbackend.Servicios;

import org.example.meatballbackend.Dto.PerfilDTO;
import org.example.meatballbackend.Entidades.Perfil;
import org.example.meatballbackend.Entidades.Usuario;
import org.example.meatballbackend.Repositorios.PerfilRepository;
import org.example.meatballbackend.Repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PerfilService implements IPerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

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
            dto.setId(p.getId());
            dto.setUsername(p.getUsername());
            dto.setNombre(p.getNombre());
            dto.setApellidos(p.getApellidos());
            dto.setFotoPerfilLink(p.getFotoPerfilLink());
            dto.setEmail(p.getEmail());
            dto.setTelefono(p.getTelefono());
            perfilDTOS.add(dto);
        }

        return perfilDTOS;
    }

    public PerfilDTO updatePerfil(Perfil perfilLogueado, PerfilDTO perfilDTO) {
        perfilLogueado.setNombre(perfilDTO.getNombre());
        perfilLogueado.setApellidos(perfilDTO.getApellidos());
        perfilLogueado.setFotoPerfilLink(perfilDTO.getFotoPerfilLink());
        perfilLogueado.setEmail(perfilDTO.getEmail());
        perfilLogueado.setTelefono(perfilDTO.getTelefono());
        Perfil perfilActualizado = perfilRepository.save(perfilLogueado);

        Usuario usuario = perfilLogueado.getUsuario();
        if (usuario != null) {
            usuario.setEmail(perfilDTO.getEmail());
            usuarioRepository.save(usuario);
        }

        return mapToDTO(perfilActualizado);
    }

    private PerfilDTO mapToDTO(Perfil perfil) {
        PerfilDTO dto = new PerfilDTO();
        dto.setId(perfil.getId());
        dto.setUsername(perfil.getUsername());
        dto.setNombre(perfil.getNombre());
        dto.setApellidos(perfil.getApellidos());
        dto.setFotoPerfilLink(perfil.getFotoPerfilLink());
        dto.setEmail(perfil.getEmail());
        dto.setTelefono(perfil.getTelefono());
        return dto;
    }
}