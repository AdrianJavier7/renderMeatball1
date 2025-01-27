package org.example.meatballbackend.Servicios;

import org.example.meatballbackend.Dto.PublicacionDTO;
import org.example.meatballbackend.Entidades.Etiqueta;
import org.example.meatballbackend.Entidades.Perfil;
import org.example.meatballbackend.Entidades.Publicacion;
import org.example.meatballbackend.Entidades.Usuario;
import org.example.meatballbackend.Enums.Rol;
import org.example.meatballbackend.Repositorios.EtiquetaRepository;
import org.example.meatballbackend.Repositorios.PublicacionRepository;
import org.example.meatballbackend.Repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private EtiquetaService etiquetaService;

    @Autowired
    private EtiquetaRepository etiquetaRepository;

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

    public Publicacion crearPublicacionDTO(PublicacionDTO publicacion, Perfil perfil){

        List<Etiqueta> todasLasEtiquetas = etiquetaService.obtenerEtiquetas();
        List<Etiqueta> etiquetasDePublicacion = new ArrayList<>();

        if(publicacion.getEtiquetas() != null){
            for (String etiqueta : publicacion.getEtiquetas()) {

                Etiqueta etiquetaEncontrada = todasLasEtiquetas.stream().filter(e -> e.getNombre().equals(etiqueta)).findFirst().orElse(null);
                etiquetasDePublicacion.add(etiquetaEncontrada);

                if(etiquetaEncontrada == null){
                    Etiqueta nuevaEtiqueta = new Etiqueta();
                    nuevaEtiqueta.setNombre(etiqueta);
                    etiquetaRepository.save(nuevaEtiqueta);

                    Publicacion publicacionSinDto = new Publicacion();

                    publicacionSinDto.setTitulo(publicacion.getTitulo());
                    publicacionSinDto.setImagenLink(publicacion.getImagenLink());
                    publicacionSinDto.setDescripcion(publicacion.getDescripcion());
                    publicacionSinDto.setReceta(publicacion.getReceta());
                    publicacionSinDto.setDificultad(publicacion.getDificultad());
                    publicacionSinDto.setTiempoPreparacion(publicacion.getTiempoPreparacion());
                    publicacionSinDto.setTiempoCoccion(publicacion.getTiempoCoccion());
                    publicacionSinDto.setRaciones(publicacion.getRaciones());
                    publicacionSinDto.setUsuario(perfil.getUsuario());
                    publicacionSinDto.setEtiquetas(etiquetasDePublicacion);
                    publicacionRepository.save(publicacionSinDto);
                    return publicacionSinDto;
                } else{

                    Publicacion publicacionSinDto = new Publicacion();

                    publicacionSinDto.setTitulo(publicacion.getTitulo());
                    publicacionSinDto.setImagenLink(publicacion.getImagenLink());
                    publicacionSinDto.setDescripcion(publicacion.getDescripcion());
                    publicacionSinDto.setReceta(publicacion.getReceta());
                    publicacionSinDto.setDificultad(publicacion.getDificultad());
                    publicacionSinDto.setTiempoPreparacion(publicacion.getTiempoPreparacion());
                    publicacionSinDto.setTiempoCoccion(publicacion.getTiempoCoccion());
                    publicacionSinDto.setRaciones(publicacion.getRaciones());
                    publicacionSinDto.setUsuario(perfil.getUsuario());
                    publicacionSinDto.setEtiquetas(etiquetasDePublicacion);
                    publicacionRepository.save(publicacionSinDto);
                    return publicacionSinDto;
                }
            }
        }
        return null;
    }
}