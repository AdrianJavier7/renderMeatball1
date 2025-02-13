package org.example.meatballbackend.Servicios;

import org.example.meatballbackend.Dto.ComentarioDTO;
import org.example.meatballbackend.Dto.IngredienteDTO;
import org.example.meatballbackend.Dto.PublicacionDTO;
import org.example.meatballbackend.Dto.VerIngredientesDTO;
import org.example.meatballbackend.Entidades.*;
import org.example.meatballbackend.Enums.Rol;
import org.example.meatballbackend.Repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PublicacionService implements IPublicacionService {

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EtiquetaService etiquetaService;

    @Autowired
    private EtiquetaRepository etiquetaRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;
    @Autowired
    private IngredienteRepository ingredienteRepository;


    // Crear una publicación
    @Override
    public Publicacion crearPublicacion(PublicacionDTO publicacionDTO) {
        Publicacion publicacion = new Publicacion();
        publicacion.setTitulo(publicacionDTO.getTitulo());
        publicacion.setImagenLink(publicacionDTO.getImagenLink());
        publicacion.setDescripcion(publicacionDTO.getDescripcion());
        publicacion.setReceta(publicacionDTO.getReceta());
        publicacion.setDificultad(publicacionDTO.getDificultad());
        publicacion.setTiempoPreparacion(publicacionDTO.getTiempoPreparacion());
        publicacion.setTiempoCoccion(publicacionDTO.getTiempoCoccion());
        publicacion.setRaciones(publicacionDTO.getRaciones());

        Usuario usuario = usuarioRepository.findById(publicacionDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        publicacion.setUsuario(usuario);

        if (publicacionDTO.getIngredientes() != null) {
            List<PublicacionIngrediente> publicacionIngredientes = new ArrayList<>();
            for (IngredienteDTO ingredienteDTO : publicacionDTO.getIngredientes()) {
                Ingrediente ingrediente = ingredienteRepository.findByNombre(ingredienteDTO.getNombre())
                        .orElseGet(() -> {
                            Ingrediente newIngrediente = new Ingrediente();
                            newIngrediente.setNombre(ingredienteDTO.getNombre());
                            return ingredienteRepository.save(newIngrediente);
                        });

                PublicacionIngrediente publicacionIngrediente = new PublicacionIngrediente();
                publicacionIngrediente.setIngrediente(ingrediente);
                publicacionIngrediente.setNombre(ingrediente.getNombre());
                publicacionIngrediente.setCantidad(ingredienteDTO.getCantidad());
                publicacionIngrediente.setTipoCantidad(ingredienteDTO.getUnidad());
                publicacionIngrediente.setPublicacion(publicacion);
                publicacionIngredientes.add(publicacionIngrediente);
            }
            publicacion.setIngredientes(publicacionIngredientes);
        }

        if (publicacionDTO.getEtiquetas() != null) {
            List<PublicacionEtiqueta> publicacionEtiquetas = publicacionDTO.getEtiquetas().stream().map(etiquetaDTO -> {
                Etiqueta etiqueta = etiquetaRepository.findByNombre(etiquetaDTO.getNombre())
                        .orElseGet(() -> {
                            Etiqueta newEtiqueta = new Etiqueta();
                            newEtiqueta.setNombre(etiquetaDTO.getNombre());
                            return etiquetaRepository.save(newEtiqueta);
                        });

                PublicacionEtiqueta publicacionEtiqueta = new PublicacionEtiqueta();
                publicacionEtiqueta.setEtiqueta(etiqueta);
                publicacionEtiqueta.setNombre(etiqueta.getNombre());
                publicacionEtiqueta.setPublicacion(publicacion);
                return publicacionEtiqueta;
            }).collect(Collectors.toList());

            publicacion.setEtiquetas(publicacionEtiquetas);
        }

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

    public Comentario comentar(Perfil perfil, ComentarioDTO comentarioDTO) {
        Comentario comentario = new Comentario();
        comentario.setComentario(comentarioDTO.getComentario());
        comentario.setFecha(comentarioDTO.getFecha().toString());
        comentario.setUsuario(perfil.getUsuario());
        comentario.setPublicacion(publicacionRepository.findById(comentarioDTO.getIdPublicacion()).orElseThrow(() -> new RuntimeException("Publicación no encontrada")));

        comentarioRepository.save(comentario);

        return comentario;
    }



    // Obtener todos los ingredientes de todas las publicaciones
    public List<String> getIngredientes(Perfil perfil) {
        List<Ingrediente> ingredientes = ingredienteRepository.findAll();
        List<String> listaDTO = new ArrayList<>();

        for (Ingrediente ingrediente : ingredientes) {
            listaDTO.add(ingrediente.getNombre());
        }

        return listaDTO;
    }


    // Obtener todas las etiquetas de todas las publicaciones
    public List<String> getEtiquetas(Perfil perfil) {
        List<Etiqueta> etiquetas = etiquetaRepository.findAll();
        List<String> listaDTO = new ArrayList<>();

        for (Etiqueta etiqueta : etiquetas) {
            listaDTO.add(etiqueta.getNombre());
        }

        return listaDTO;
    }


    // Eliminar un ingrediente de una publicación
    public boolean eliminarIngrediente(int idPublicacion, Perfil perfil, String ingrediente) {
        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(idPublicacion);

        if (publicacionOpt.isPresent()) {
            Publicacion publicacion = publicacionOpt.get();
            boolean removed = publicacion.getIngredientes().remove(ingrediente);
            if (removed) {
                publicacionRepository.save(publicacion);
                return true;
            }
        }
        return false;
    }
}