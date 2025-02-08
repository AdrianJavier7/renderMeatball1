package org.example.meatballbackend.Servicios;

import org.example.meatballbackend.Dto.PublicacionDTO;
import org.example.meatballbackend.Entidades.Publicacion;

import java.util.List;

public interface IPublicacionService {
    // Crear una publicación
    Publicacion crearPublicacion(Publicacion publicacion);

    // Obtener todas las publicaciones que hay
    List<Publicacion> obtenerPublicaciones();

    // Borrar una publicación
    void borrarPublicacion(int publicacionId, int usuarioId);

    List<PublicacionDTO> getAll();
}