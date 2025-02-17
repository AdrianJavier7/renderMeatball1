package org.example.meatballbackend.Servicios;

import org.example.meatballbackend.Dto.PerfilDTO;
import org.example.meatballbackend.Entidades.Perfil;
import org.example.meatballbackend.Entidades.Usuario;
import org.example.meatballbackend.Enums.Estado;
import org.example.meatballbackend.Enums.Estado;
import org.example.meatballbackend.Enums.Rol;
import org.example.meatballbackend.Repositorios.PerfilRepository;
import org.example.meatballbackend.Repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PerfilService implements IPerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Perfil buscarPorId(Integer id){
        return perfilRepository.findById(id).orElseThrow();
    }

    public Perfil buscarPorUsuario(Usuario usuario){
        return perfilRepository.findTopByUsuario(usuario);
    }

    public Perfil guardarPerfil(Perfil perfil){
        return perfilRepository.save(perfil);
    }

    public PerfilDTO getPerfilById(Integer id){
        Perfil perfil = perfilRepository.findById(id).orElseThrow();
        return this.miPerfilDTO(perfil);
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

        return miPerfilDTO(perfilActualizado);
    }


    public PerfilDTO miPerfilDTO(Perfil perfil){
        PerfilDTO dto = new PerfilDTO();
        dto.setNombre(perfil.getNombre());
        dto.setApellidos(perfil.getApellidos());
        dto.setFotoPerfilLink(perfil.getFotoPerfilLink());
        dto.setEmail(perfil.getEmail());
        dto.setTelefono(perfil.getTelefono());
        dto.setUsername(perfil.getUsername());
        dto.setId(perfil.getId());
        return dto;
    }

    public boolean isAdmin(Perfil perfil){
        return perfil.getUsuario().getRol() == Rol.Admin;
    }

    public PerfilDTO setActivo(Integer id){
        Perfil perfil = perfilRepository.findById(id).orElseThrow();
        perfil.setEstado(Estado.Activo);
        perfil.getUsuario().setEstado(Estado.Activo);
        perfilRepository.save(perfil);
        return miPerfilDTO(perfil);
    }

    public PerfilDTO setBaneado(Integer id){
        Perfil perfil = perfilRepository.findById(id).orElseThrow();
        perfil.setEstado(Estado.Baneado);
        perfil.getUsuario().setEstado(Estado.Baneado);
        perfilRepository.save(perfil);
        return miPerfilDTO(perfil);
    }

    public PerfilDTO setPendienteRevision(Integer id){
        Perfil perfil = perfilRepository.findById(id).orElseThrow();
        perfil.setEstado(Estado.Pendiente_revision);
        perfil.getUsuario().setEstado(Estado.Pendiente_revision);
        perfilRepository.save(perfil);
        return miPerfilDTO(perfil);
    }


    public int contarSeguidos(int usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        Usuario usuario = usuarioOpt.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuario.getSeguidos().size();
    }

    public int contarSeguidores(Integer usuarioId) {
        return usuarioRepository.contarTotalSeguidores(usuarioId);
    }

    public boolean isUsuarioBaneado(Integer perfilId) {
        Perfil perfil = perfilRepository.findById(perfilId).orElseThrow();
        Estado estado = perfil.getEstado();
        return Estado.Baneado.equals(estado);
    }
}