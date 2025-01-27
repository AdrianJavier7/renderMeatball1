package org.example.meatballbackend.Controladores;

import org.example.meatballbackend.Dto.PerfilDTO;
import org.example.meatballbackend.Servicios.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/perfil")
public class PerfilController {
    @Autowired
    private PerfilService perfilService;

    @GetMapping("/all")
    public List<PerfilDTO> getAllPerfiles(){
        List<PerfilDTO> perfiles = perfilService.getAll();
        return perfiles;
    }
}
