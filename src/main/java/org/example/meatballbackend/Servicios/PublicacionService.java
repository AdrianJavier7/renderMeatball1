package org.example.meatballbackend.Servicios;

import org.example.meatballbackend.Dto.ComentarioDTO;
import org.example.meatballbackend.Dto.ComentarioRecibidoDTO;
import org.example.meatballbackend.Dto.PublicacionDTO;
import org.example.meatballbackend.Entidades.*;
import org.example.meatballbackend.Enums.Rol;
import org.example.meatballbackend.Repositorios.ComentarioRepository;
import org.example.meatballbackend.Repositorios.EtiquetaRepository;
import org.example.meatballbackend.Repositorios.PublicacionRepository;
import org.example.meatballbackend.Repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PublicacionService implements IPublicacionService {

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    // Crear una publicación
    @Override
    public Publicacion crearPublicacion(Publicacion publicacion) {
        return publicacionRepository.save(publicacion);
    }

    @Override
    public List<Publicacion> obtenerPublicaciones() {
        return publicacionRepository.findAll();
    }


    public List<Publicacion> obtenerPublicacionesUsuario(Integer idUsuario) {
        return publicacionRepository.findByUsuarioId(idUsuario);
    }


    @Override
    public void borrarPublicacion(int publicacionId, int usuarioId) {
        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(publicacionId);
        if (publicacionOpt.isPresent()) {
            Publicacion publicacion = publicacionOpt.get();
            Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            if (publicacion.getUsuario().getId().equals(usuarioId) || usuario.getRol() == Rol.Admin) {
                publicacionRepository.delete(publicacion);
            } else {
                throw new RuntimeException("No tienes permiso para borrar esta publicación");
            }
        } else {
            throw new RuntimeException("Publicación no encontrada");
        }
    }

    public List<PublicacionDTO> getPublicacionesParaTi (Perfil perfil){
        List<Publicacion> publicaciones = publicacionRepository.findAllExceptByUsuario(perfil.getUsuario());
        List<PublicacionDTO> publicacionDTOList = new ArrayList<>();

        for (Publicacion publicacion : publicaciones) {
            PublicacionDTO publicacionDTO = new PublicacionDTO();
            publicacionDTO.setId(publicacion.getId());
            publicacionDTO.setTitulo(publicacion.getTitulo());
            publicacionDTO.setImagenLink(publicacion.getImagenLink());
            publicacionDTO.setDescripcion(publicacion.getDescripcion());
            publicacionDTO.setReceta(publicacion.getReceta());
            publicacionDTO.setDificultad(publicacion.getDificultad());
            publicacionDTO.setTiempoPreparacion(publicacion.getTiempoPreparacion());
            publicacionDTO.setTiempoCoccion(publicacion.getTiempoCoccion());
            publicacionDTO.setRaciones(publicacion.getRaciones());
            publicacionDTOList.add(publicacionDTO);
        }

        return  publicacionDTOList;
    }

    public List<PublicacionDTO> getAll() {
        List<Publicacion> publicaciones = publicacionRepository.findAll();
        List<PublicacionDTO> publicacionDTOS = new ArrayList<>();

        for (Publicacion p : publicaciones) {
            PublicacionDTO dto = new PublicacionDTO();
            dto.setId(p.getId());
            dto.setUsuarioId(p.getUsuario().getId());
            dto.setUsername(p.getUsuario().getUsername());
            publicacionDTOS.add(dto);
        }

        return publicacionDTOS;
    }

    public void darLike(Perfil perfil, int publicacionId) {
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        if (publicacion.getLikes().contains(perfil.getUsuario())) {
            publicacion.getLikes().remove(perfil.getUsuario());
        } else {
            publicacion.getLikes().add(perfil.getUsuario());
        }

        publicacionRepository.save(publicacion);
    }

    public void quitarLike(Perfil perfil, int publicacionId) {
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        if (publicacion.getLikes().contains(perfil.getUsuario())) {
            publicacion.getLikes().remove(perfil.getUsuario());
        }

        publicacionRepository.save(publicacion);
    }

    public ComentarioDTO comentar(Perfil perfil, ComentarioRecibidoDTO comentarioDTO) {
        Comentario comentario = new Comentario();
        comentario.setComentario(comentarioDTO.comentarioTexto);
        comentario.setFecha(String.valueOf(LocalDateTime.now()));
        comentario.setPerfil(perfil);
        comentario.setPublicacion(publicacionRepository.findById(comentarioDTO.idPublicacion).orElseThrow(() -> new RuntimeException("Publicación no encontrada")));

        comentarioRepository.save(comentario);

        ComentarioDTO comentarioResponseDTO = new ComentarioDTO();
        comentarioResponseDTO.setComentario(comentario.getComentario());
        comentarioResponseDTO.setFecha(comentario.getFecha());
        comentarioResponseDTO.setNombreUsuario(comentario.getPerfil().getUsuario().getUsername());
        comentarioResponseDTO.setIdPublicacion(comentario.getPublicacion().getId());

        return comentarioResponseDTO;
    }

    public List<ComentarioDTO> getComentarios(int publicacionId) {

        List<Comentario> comentarios = comentarioRepository.findByPublicacionId(publicacionId);
        List<ComentarioDTO> comentarioDTOS = new ArrayList<>();

        for (Comentario c : comentarios) {
            ComentarioDTO dto = new ComentarioDTO();
            dto.setComentario(c.getComentario());
            dto.setFecha(c.getFecha());
            dto.setFotoUsuario(c.getPerfil().getFotoPerfilLink());
            dto.setNombreUsuario(c.getPerfil().getNombre());
            dto.setIdPublicacion(c.getPublicacion().getId());
            comentarioDTOS.add(dto);
        }

        return comentarioDTOS;
    }
}