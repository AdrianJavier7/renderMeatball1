package org.example.meatballbackend.Controladores;

import lombok.AllArgsConstructor;
import org.example.meatballbackend.Dto.PerfilDTO;
import org.example.meatballbackend.Dto.PublicacionDTO;
import org.example.meatballbackend.Entidades.Perfil;
import org.example.meatballbackend.Security.JWTService;
import org.example.meatballbackend.Servicios.PublicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
