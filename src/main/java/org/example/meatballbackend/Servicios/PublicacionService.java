package org.example.meatballbackend.Servicios;

import org.example.meatballbackend.Dto.PerfilDTO;
import org.example.meatballbackend.Dto.ComentarioDTO;
import org.example.meatballbackend.Dto.ComentarioRecibidoDTO;
import org.example.meatballbackend.Dto.IngredienteDTO;
import org.example.meatballbackend.Dto.PublicacionDTO;
import org.example.meatballbackend.Entidades.Comentario;
import org.example.meatballbackend.Entidades.Perfil;
import org.example.meatballbackend.Entidades.Publicacion;
import org.example.meatballbackend.Entidades.Usuario;
import org.example.meatballbackend.Dto.VerIngredientesDTO;
import org.example.meatballbackend.Entidades.*;
import org.example.meatballbackend.Enums.Rol;
import org.example.meatballbackend.Repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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

    @Autowired
    private PerfilRepository perfilRepository;


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

        Perfil perfil = perfilRepository.findById(publicacionDTO.getPerfilId())
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
        publicacion.setPerfil(perfil);

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

    public Publicacion buscarPorUsuario(Usuario usuario){
        return publicacionRepository.findTopByUsuario(usuario);
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
            publicacionDTO.setUsuarioId(publicacion.getUsuario().getId());
            publicacionDTO.setUsername(publicacion.getUsuario().getUsername());
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

            Perfil perfil = p.getPerfil();
            if (perfil != null) {
                dto.setFotoPerfilLink(perfil.getFotoPerfilLink());
            } else {
                dto.setFotoPerfilLink(null);
            }

            publicacionDTOS.add(dto);
        }

        return publicacionDTOS;
    }

    public List<PublicacionDTO> convertirAListaDTO(List<Publicacion> publicaciones) {
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
            publicacionDTO.setUsuarioId(publicacion.getUsuario().getId());
            publicacionDTO.setUsername(publicacion.getUsuario().getUsername());
            publicacionDTOList.add(publicacionDTO);
        }
        return publicacionDTOList;
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

    public List<PublicacionDTO> getPublicacionesPorUsuarioId(Integer idUsuario) {
        List<Publicacion> publicaciones = publicacionRepository.findByUsuarioId(idUsuario);
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
            publicacionDTO.setUsuarioId(publicacion.getUsuario().getId());
            publicacionDTO.setUsername(publicacion.getUsuario().getUsername());
            publicacionDTOList.add(publicacionDTO);
        }
        return publicacionDTOList;
    }

    public List<ComentarioDTO> getComentarios(int publicacionId) {

        List<Comentario> comentarios = comentarioRepository.findByPublicacionId(publicacionId);
        List<ComentarioDTO> comentarioDTOS = new ArrayList<>();

        for (Comentario c : comentarios) {
            ComentarioDTO dto = new ComentarioDTO();
            dto.setComentario(c.getComentario());
            dto.setFecha(c.getFecha());
            dto.setFotoUsuario(c.getPerfil().getFotoPerfilLink());
            dto.setNombreUsuario(c.getPerfil().getUsuario().getUsername());
            dto.setIdPublicacion(c.getPublicacion().getId());
            comentarioDTOS.add(dto);
        }

        return comentarioDTOS;
    }

    public List<PublicacionDTO> getPublicacionesUsuario(Perfil perfil){
        List<Publicacion> publicaciones = publicacionRepository.findByUsuarioId(perfil.getUsuario().getId());
        List<PublicacionDTO> publicacionDTOS = new ArrayList<>();

        for (Publicacion p : publicaciones) {
            PublicacionDTO dto = new PublicacionDTO();
            dto.setId(p.getId());
            dto.setTitulo(p.getTitulo());
            dto.setImagenLink(p.getImagenLink());
            dto.setDescripcion(p.getDescripcion());
            dto.setReceta(p.getReceta());
            dto.setDificultad(p.getDificultad());
            dto.setTiempoPreparacion(p.getTiempoPreparacion());
            dto.setTiempoCoccion(p.getTiempoCoccion());
            dto.setRaciones(p.getRaciones());
            publicacionDTOS.add(dto);
        }

        return publicacionDTOS;
    }

    public List<PublicacionDTO> getPublicacionesDeSeguidos(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Integer> seguidosIds = usuario.getSeguidos().stream()
                .map(Usuario::getId)
                .collect(Collectors.toList());

        List<Publicacion> publicaciones = publicacionRepository.findByUsuarioIdIn(seguidosIds);
        return convertirAListaDTO(publicaciones);
    }

    public List<PublicacionDTO> getPublicacionesAleatorias(Perfil perfilLogueado) {
        List<Publicacion> publicaciones = publicacionRepository.findAll().stream()
                .filter(publicacion -> !publicacion.getUsuario().getId().equals(perfilLogueado.getUsuario().getId()))
                .collect(Collectors.toList());
        Collections.shuffle(publicaciones);
        List<Publicacion> publicacionesAleatorias = publicaciones.stream().limit(8).collect(Collectors.toList());
        return convertirAListaDTO(publicacionesAleatorias);
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