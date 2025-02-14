package org.example.meatballbackend.Controladores;

import org.example.meatballbackend.Dto.PerfilDTO;
import org.example.meatballbackend.Dto.PublicacionDTO;
import org.example.meatballbackend.Entidades.Perfil;
import org.example.meatballbackend.Entidades.Publicacion;
import org.example.meatballbackend.Security.JWTService;
import org.example.meatballbackend.Servicios.PerfilService;
import org.example.meatballbackend.Servicios.PublicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // Import the Map class

@RestController
@RequestMapping("/perfil")
public class PerfilController {
    @Autowired
    private PerfilService perfilService;

    @Autowired
    private PublicacionService publiconService;

    @Autowired
    private JWTService jwtService;

    @GetMapping("/all")
    public List<PerfilDTO> getAllPerfiles(){
        List<PerfilDTO> perfiles = perfilService.getAll();
        return perfiles;
    }

    @GetMapping("/{id}")
    public PerfilDTO getPerfilById(@PathVariable Integer id){
        return perfilService.getPerfilById(id);
    }


    @PutMapping("/update")
    public PerfilDTO updatePerfil(@RequestHeader("Authorization") String token, @RequestBody PerfilDTO perfilDTO) {
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        return perfilService.updatePerfil(perfilLogueado, perfilDTO);
    }

    @GetMapping("/miPerfil")
    public PerfilDTO getPerfil(@RequestHeader("Authorization") String token){
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        return perfilService.miPerfilDTO(perfilLogueado);
    }


    @GetMapping("/misPublicaciones")
    public List<PublicacionDTO> getPublicaciones(@RequestHeader("Authorization") String token) {
        List<Publicacion> publicaciones = jwtService.extraerPublicacionesToken(token);
        return publiconService.convertirAListaDTO(publicaciones);
    }

    @GetMapping("/otrasPublicaciones/{idUsuario}")
    public List<PublicacionDTO> getPublicacionesPorUsuarioId(@PathVariable Integer idUsuario) {
        return publiconService.getPublicacionesPorUsuarioId(idUsuario);
    }

    @GetMapping("/seguidos/{id}")
    public int contarSeguidos(@PathVariable Integer id) {
        return perfilService.contarSeguidos(id);
    }

    @GetMapping("/seguidores/{id}")
    public int contarSeguidores(@PathVariable Integer id) {
        return perfilService.contarSeguidores(id);
    }

    @GetMapping("/seguidosPerfil")
    public int contarSeguidosPerfil(@RequestHeader("Authorization") String token) {
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        return perfilService.contarSeguidos(perfilLogueado.getUsuario().getId());
    }

    @GetMapping("/seguidoresPerfil")
    public int contarSeguidoresPerfil(@RequestHeader("Authorization") String token) {
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        return perfilService.contarSeguidores(perfilLogueado.getUsuario().getId());
    }
}