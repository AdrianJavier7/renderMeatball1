package org.example.meatballbackend.Controladores;

import org.example.meatballbackend.Dto.PerfilDTO;
import org.example.meatballbackend.Entidades.Perfil;
import org.example.meatballbackend.Security.JWTService;
import org.example.meatballbackend.Servicios.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/perfil")
public class PerfilController {
    @Autowired
    private PerfilService perfilService;

    @Autowired
    private JWTService jwtService;

    @GetMapping("/all")
    public List<PerfilDTO> getAllPerfiles(){
        List<PerfilDTO> perfiles = perfilService.getAll();
        return perfiles;
    }

    @GetMapping("/miPerfil")
    public PerfilDTO getPerfil(@RequestHeader("Authorization") String token){
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        return perfilService.miPerfilDTO(perfilLogueado);
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
}
