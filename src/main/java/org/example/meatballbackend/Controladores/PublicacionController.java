package org.example.meatballbackend.Controladores;

import lombok.AllArgsConstructor;
import org.example.meatballbackend.Dto.ComentarioDTO;
import org.example.meatballbackend.Dto.ComentarioRecibidoDTO;
import org.example.meatballbackend.Dto.PublicacionDTO;
import org.example.meatballbackend.Entidades.Comentario;
import org.example.meatballbackend.Entidades.Perfil;
import org.example.meatballbackend.Entidades.Publicacion;
import org.example.meatballbackend.Security.JWTService;
import org.example.meatballbackend.Servicios.PublicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publicacion")
@AllArgsConstructor
public class PublicacionController {

    @Autowired
    PublicacionService publicacionService;

    @Autowired
    private JWTService jwtService;

    @GetMapping("/parati")
    public List<PublicacionDTO> getPublicacionesParaTi(@RequestHeader("Authorization") String token) {
        Perfil perfiLogueado = jwtService.extraerPerfilToken(token);
        return publicacionService.getPublicacionesParaTi(perfiLogueado);
    }

    @GetMapping("/all")
    public List<PublicacionDTO> getAllPublicaciones(){
        List<PublicacionDTO> publicaciones = publicacionService.getAll();
        return publicaciones;
    }

    @GetMapping("publicacionesUsuario")
    public List<PublicacionDTO> getPublicacionesUsuario(@RequestHeader("Authorization") String token){

        return null;
    }

    @PostMapping("/like")
    public void darLike(@RequestParam int idPublicacion, @RequestHeader("Authorization") String token) {
        Perfil perfiLogueado = jwtService.extraerPerfilToken(token);
        publicacionService.darLike(perfiLogueado, idPublicacion );
    }

    @PostMapping("/quitarlike")
    public void quitarLike(@RequestParam int idPublicacion, @RequestHeader("Authorization") String token) {
        Perfil perfiLogueado = jwtService.extraerPerfilToken(token);
        publicacionService.quitarLike(perfiLogueado, idPublicacion );
    }

    @PostMapping("/comentar")
    public ComentarioDTO comentar(@RequestBody ComentarioRecibidoDTO comentarioDTO, @RequestHeader("Authorization") String token) {
        Perfil perfiLogueado = jwtService.extraerPerfilToken(token);

        return publicacionService.comentar(perfiLogueado, comentarioDTO);
    }

    @GetMapping("/comentarios")
    public List<ComentarioDTO> getComentarios(@RequestParam int idPublicacion, @RequestHeader("Authorization") String token){
        return publicacionService.getComentarios(idPublicacion);
    }

    @GetMapping("/seguidos")
    public List<PublicacionDTO> getPublicacionesDeSeguidos(@RequestHeader("Authorization") String token) {
        Perfil perfiLogueado = jwtService.extraerPerfilToken(token);
        return publicacionService.getPublicacionesDeSeguidos(perfiLogueado.getUsuario().getId());
    }

    @GetMapping("/aleatorias")
    public List<PublicacionDTO> getPublicacionesAleatorias(@RequestHeader("Authorization") String token) {
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        return publicacionService.getPublicacionesAleatorias(perfilLogueado);
    }


    // Obtener todos los ingredientes
    @GetMapping("/ingredientes")
    public List<String> getIngredientes(@RequestHeader("Authorization") String token) {
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        return publicacionService.getIngredientes(perfilLogueado);
    }

    // Obtener las etiquetas
    @GetMapping("/etiquetas")
    public List<String> getEtiquetas(@RequestHeader("Authorization") String token) {
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        return publicacionService.getEtiquetas(perfilLogueado);
    }


    // Eliminar un ingrediente de una publicación
    @DeleteMapping("/{id}/ingrediente")
    public String eliminarIngrediente(@PathVariable int id, @RequestParam String ingrediente, @RequestHeader("Authorization") String token) {
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        boolean eliminado = publicacionService.eliminarIngrediente(id, perfilLogueado, ingrediente);
        return eliminado ? "Ingrediente eliminado correctamente." : "No se pudo eliminar el ingrediente.";
    }


    @PostMapping("/agregar")
    public Publicacion agregarPublicacion(@RequestBody PublicacionDTO publicacionDTO, @RequestHeader("Authorization") String token) {
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        publicacionDTO.setUsuarioId(perfilLogueado.getUsuario().getId());
        return publicacionService.crearPublicacion(publicacionDTO);
    }

}
