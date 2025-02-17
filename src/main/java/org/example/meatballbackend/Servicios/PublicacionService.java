package org.example.meatballbackend.Servicios;

import org.example.meatballbackend.Dto.*;
import org.example.meatballbackend.Entidades.Comentario;
import org.example.meatballbackend.Entidades.Perfil;
import org.example.meatballbackend.Entidades.Publicacion;
import org.example.meatballbackend.Entidades.Usuario;
import org.example.meatballbackend.Entidades.*;
import org.example.meatballbackend.Enums.Estado;
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
        publicacion.setEstado(Estado.Activo);

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

    public List<PublicacionDTO> getPublicacionesParaTi(Perfil perfil) {
        List<Publicacion> publicaciones = publicacionRepository.findAllExceptByUsuario(perfil.getUsuario());
        List<PublicacionDTO> publicacionDTOList = new ArrayList<>();


        for (Publicacion publicacion : publicaciones) {
            if (publicacion.getEstado() == Estado.Activo || publicacion.getEstado() == Estado.Pendiente_revision) {
                PublicacionDTO publicacionDTO = new PublicacionDTO();
                publicacionDTO.setId(publicacion.getId());
                publicacionDTO.setTitulo(publicacion.getTitulo());
                publicacionDTO.setImagenLink(publicacion.getImagenLink());
                publicacionDTO.setDescripcion(publicacion.getDescripcion());
                publicacionDTO.setReceta(publicacion.getReceta());
                publicacionDTO.setDificultad(publicacion.getDificultad());
                publicacionDTO.setTiempoPreparacion(publicacion.getTiempoPreparacion());
                publicacionDTO.setTiempoCoccion(publicacion.getTiempoCoccion());
                publicacionDTO.setEstado(publicacion.getEstado());
                publicacionDTO.setRaciones(publicacion.getRaciones());
                publicacionDTO.setUsuarioId(publicacion.getUsuario().getId());
                publicacionDTO.setUsername(publicacion.getUsuario().getUsername());

                List<IngredienteDTO> ingredientes = publicacion.getIngredientes().stream()
                        .map(pi -> new IngredienteDTO(pi.getIngrediente().getNombre(), pi.getCantidad(), pi.getTipoCantidad()))
                        .collect(Collectors.toList());

                publicacionDTO.setIngredientes(ingredientes);

                List<EtiquetaDTO> etiquetas = publicacion.getEtiquetas().stream()
                        .map(pe -> new EtiquetaDTO(pe.getEtiqueta().getId(), pe.getEtiqueta().getNombre()))
                        .collect(Collectors.toList());

                publicacionDTO.setEtiquetas(etiquetas);

                publicacionDTOList.add(publicacionDTO);
            }
        }

        return publicacionDTOList;
    }

    public List<PublicacionDTO> getAll() {
        List<Publicacion> publicaciones = publicacionRepository.findAll();
        List<PublicacionDTO> publicacionDTOS = new ArrayList<>();

        for (Publicacion p : publicaciones) {
            if (p.getEstado() == Estado.Activo || p.getEstado() == Estado.Pendiente_revision) {
                PublicacionDTO dto = new PublicacionDTO();
                dto.setId(p.getId());
                dto.setUsuarioId(p.getUsuario().getId());
                dto.setUsername(p.getUsuario().getUsername());
                dto.setTitulo(p.getTitulo());
                dto.setDescripcion(p.getDescripcion());
                dto.setImagenLink(p.getImagenLink());
                dto.setReceta(p.getReceta());
                dto.setDificultad(p.getDificultad());
                dto.setTiempoPreparacion(p.getTiempoPreparacion());
                dto.setTiempoCoccion(p.getTiempoCoccion());
                dto.setRaciones(p.getRaciones());
                dto.setEstado(p.getEstado());

                Perfil perfil = p.getPerfil();
                if (perfil != null) {
                    dto.setFotoPerfilLink(perfil.getFotoPerfilLink());
                } else {
                    dto.setFotoPerfilLink(null);
                }

                publicacionDTOS.add(dto);
            }
        }

        return publicacionDTOS;
    }

    public List<PublicacionDTO> convertirAListaDTO(List<Publicacion> publicaciones) {
        List<PublicacionDTO> publicacionDTOList = new ArrayList<>();

        for (Publicacion publicacion : publicaciones) {
            if (publicacion.getEstado() == Estado.Activo || publicacion.getEstado() == Estado.Pendiente_revision) {
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
                publicacionDTO.setEstado(publicacion.getEstado());
                publicacionDTO.setUsuarioId(publicacion.getUsuario().getId());
                publicacionDTO.setUsername(publicacion.getUsuario().getUsername());
                publicacionDTOList.add(publicacionDTO);

                List<IngredienteDTO> ingredientes = publicacion.getIngredientes().stream()
                        .map(pi -> new IngredienteDTO(pi.getIngrediente().getNombre(), pi.getCantidad(), pi.getTipoCantidad()))
                        .collect(Collectors.toList());

                publicacionDTO.setIngredientes(ingredientes);

                List<EtiquetaDTO> etiquetas = publicacion.getEtiquetas().stream()
                        .map(pe -> new EtiquetaDTO(pe.getEtiqueta().getId(), pe.getEtiqueta().getNombre()))
                        .collect(Collectors.toList());

                publicacionDTO.setEtiquetas(etiquetas);

                System.out.println("Nombre de la publicacion: " + publicacionDTO.getTitulo() + "Ingredientes: " + publicacionDTO.getIngredientes());
            }
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
            if (publicacion.getEstado() == Estado.Activo || publicacion.getEstado() == Estado.Pendiente_revision) {
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

                List<IngredienteDTO> ingredientes = publicacion.getIngredientes().stream()
                        .map(pi -> new IngredienteDTO(pi.getIngrediente().getNombre(), pi.getCantidad(), pi.getTipoCantidad()))
                        .collect(Collectors.toList());

                publicacionDTO.setIngredientes(ingredientes);

                List<EtiquetaDTO> etiquetas = publicacion.getEtiquetas().stream()
                        .map(pe -> new EtiquetaDTO(pe.getEtiqueta().getId(), pe.getEtiqueta().getNombre()))
                        .collect(Collectors.toList());

                publicacionDTO.setEtiquetas(etiquetas);
            }
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

    public PublicacionDTO miPublicacionDTO(Publicacion publicacion){
        PublicacionDTO dto = new PublicacionDTO();
        dto.setId(publicacion.getId());
        dto.setTitulo(publicacion.getTitulo());
        dto.setImagenLink(publicacion.getImagenLink());
        dto.setDescripcion(publicacion.getDescripcion());
        dto.setReceta(publicacion.getReceta());
        dto.setDificultad(publicacion.getDificultad());
        dto.setTiempoPreparacion(publicacion.getTiempoPreparacion());
        dto.setTiempoCoccion(publicacion.getTiempoCoccion());
        dto.setRaciones(publicacion.getRaciones());
        dto.setEstado(publicacion.getEstado());
        dto.setUsuarioId(publicacion.getUsuario().getId());
        dto.setUsername(publicacion.getUsuario().getUsername());
        return dto;
    }

    public PublicacionDTO setActivo(Integer id){
        Publicacion publicacion = publicacionRepository.findById(id).orElseThrow();
        publicacion.setEstado(Estado.Activo);
        publicacion.getUsuario().setEstado(Estado.Activo);
        publicacionRepository.save(publicacion);
        return miPublicacionDTO(publicacion);
    }

    public PublicacionDTO setBaneado(Integer id){
        Publicacion publicacion = publicacionRepository.findById(id).orElseThrow();
        publicacion.setEstado(Estado.Baneado);
        publicacion.getUsuario().setEstado(Estado.Baneado);
        publicacionRepository.save(publicacion);
        return miPublicacionDTO(publicacion);
    }

    public PublicacionDTO setPendienteRevision(Integer id){
        Publicacion publicacion = publicacionRepository.findById(id).orElseThrow();
        publicacion.setEstado(Estado.Pendiente_revision);
        publicacion.getUsuario().setEstado(Estado.Pendiente_revision);
        publicacionRepository.save(publicacion);
        return miPublicacionDTO(publicacion);
    }

    public List<PublicacionDTO> getPublicacionesDeSeguidos(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Integer> seguidosIds = usuario.getSeguidos().stream()
                .map(Usuario::getId)
                .collect(Collectors.toList());

        List<Publicacion> publicaciones = publicacionRepository.findByUsuarioIdIn(seguidosIds)
                .stream()
                .filter(publicacion -> publicacion.getEstado() == Estado.Activo || publicacion.getEstado() == Estado.Pendiente_revision)
                .collect(Collectors.toList());

        return convertirAListaDTO(publicaciones);
    }

    public List<PublicacionDTO> getPublicacionesAleatorias(Perfil perfilLogueado) {
        List<Publicacion> publicaciones = publicacionRepository.findAll().stream()
                .filter(publicacion -> !publicacion.getUsuario().getId().equals(perfilLogueado.getUsuario().getId()) && (publicacion.getEstado() == Estado.Activo || publicacion.getEstado() == Estado.Pendiente_revision))
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

    public List<PublicacionDTO> getPublicacionesPorIngredientesOEtiquetas(List<String> ingredientes, List<String> etiquetas) {
        if ((ingredientes == null || ingredientes.isEmpty()) && (etiquetas == null || etiquetas.isEmpty())) {
            throw new IllegalArgumentException("Debe proporcionar al menos un ingrediente o una etiqueta");
        }
        List<Publicacion> publicaciones = publicacionRepository.findByIngredientesOrEtiquetas(
                (ingredientes == null || ingredientes.isEmpty()) ? null : ingredientes,
                (etiquetas == null || etiquetas.isEmpty()) ? null : etiquetas
        );
        List<Publicacion> publicacionesActivas = publicaciones.stream()
                .filter(publicacion -> publicacion.getEstado() == Estado.Activo)
                .collect(Collectors.toList());
        return publicacionesActivas.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    private PublicacionDTO convertirADTO(Publicacion publicacion) {
        PublicacionDTO dto = new PublicacionDTO();
        dto.setId(publicacion.getId());
        dto.setTitulo(publicacion.getTitulo());
        dto.setImagenLink(publicacion.getImagenLink());
        dto.setDescripcion(publicacion.getDescripcion());
        dto.setReceta(publicacion.getReceta());
        dto.setDificultad(publicacion.getDificultad());
        dto.setTiempoPreparacion(publicacion.getTiempoPreparacion());
        dto.setTiempoCoccion(publicacion.getTiempoCoccion());
        dto.setRaciones(publicacion.getRaciones());
        dto.setEstado(publicacion.getEstado());
        dto.setUsuarioId(publicacion.getUsuario().getId());
        dto.setUsername(publicacion.getUsuario().getUsername());

        List<IngredienteDTO> ingredientes = publicacion.getIngredientes().stream()
                .map(pi -> new IngredienteDTO(pi.getIngrediente().getNombre(), pi.getCantidad(), pi.getTipoCantidad()))
                .collect(Collectors.toList());
        dto.setIngredientes(ingredientes);

        List<EtiquetaDTO> etiquetas = publicacion.getEtiquetas().stream()
                .map(pe -> new EtiquetaDTO(pe.getEtiqueta().getId(), pe.getEtiqueta().getNombre()))
                .collect(Collectors.toList());
        dto.setEtiquetas(etiquetas);

        return dto;
    }

    public boolean actualizarEstadoPublicacion(int idPublicacion, Estado nuevoEstado, Perfil perfilLogueado) {
        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(idPublicacion);
        if (publicacionOpt.isPresent()) {
            Publicacion publicacion = publicacionOpt.get();
            if (publicacion.getUsuario().getId().equals(perfilLogueado.getUsuario().getId())) {
                publicacion.setEstado(nuevoEstado);
                publicacionRepository.save(publicacion);
                return true;
            }
        }
        return false;
    }

    public List<PublicacionDTO> getPublicacionesBaneadas() {
        List<Publicacion> publicaciones = publicacionRepository.findByEstado(Estado.Baneado);
        return publicaciones.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public void eliminarPublicacion(Perfil perfil, Integer publicacionId) {
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        if (publicacion.getUsuario() == null) {
            throw new RuntimeException("La publicación no tiene un perfil asociado");
        }

        if (!publicacion.getUsuario().getId().equals(perfil.getUsuario().getId())) {
            throw new RuntimeException("No tienes permiso para eliminar esta publicación");
        }

        publicacionRepository.delete(publicacion);
    }
}