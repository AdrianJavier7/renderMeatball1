package org.example.meatballbackend.Controladores;

import org.example.meatballbackend.Dto.UsuarioDTO;
import org.example.meatballbackend.Servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/seguir")
    public void seguirUsuario(@RequestBody UsuarioDTO request) {
        usuarioService.seguirUsuario(request.getSeguidor_id(), request.getSeguido_id());
    }

    @PostMapping("/dejarSeguir")
    public void dejarDeSeguirUsuario(@RequestBody UsuarioDTO request) {
        usuarioService.dejarDeSeguirUsuario(request.getSeguidor_id(), request.getSeguido_id());
    }
}
