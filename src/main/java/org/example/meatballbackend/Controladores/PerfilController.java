package org.example.meatballbackend.Controladores;

import org.example.meatballbackend.Dto.PublicacionDTO;
import org.example.meatballbackend.Entidades.Perfil;
import org.example.meatballbackend.Servicios.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/perfil")
public class PerfilController {
    @Autowired
    private PerfilService perfilService;

    @GetMapping("/usuario/{id}")
    public String obtenerUsuario(@PathVariable Integer id) {
        return perfilService.obtenerUsername(id);
    }

    @GetMapping("/foto/{id}")
    public String obtenerPerfil(@PathVariable Integer id) {
        return perfilService.obtenerFotoPerfil(id);
    }
}
