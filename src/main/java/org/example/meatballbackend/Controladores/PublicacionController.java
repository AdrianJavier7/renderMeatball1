package org.example.meatballbackend.Controladores;

import lombok.AllArgsConstructor;
import org.example.meatballbackend.Dto.PublicacionDTO;
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

    PublicacionService publicacionService;

    private JWTService jwtService;

    @GetMapping("/parati")
    public List<PublicacionDTO> getPublicacionesParaTi(@RequestHeader("Authorization") String token) {
        Perfil perfiLogueado = jwtService.extraerPerfilToken(token);
        return publicacionService.getPublicacionesParaTi(perfiLogueado);
    }

    @PostMapping("/crear")
    public Publicacion crearPublicacion(@RequestBody PublicacionDTO publicacionDTO, @RequestHeader("Authorization") String token) {
        Perfil perfiLogueado = jwtService.extraerPerfilToken(token);
        return publicacionService.crearPublicacionDTO(publicacionDTO, perfiLogueado);
    }

}
