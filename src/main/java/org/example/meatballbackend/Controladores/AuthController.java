package org.example.meatballbackend.Controladores;

import lombok.AllArgsConstructor;
import org.example.meatballbackend.Dto.LoginDTO;
import org.example.meatballbackend.Dto.RegistroDTO;
import org.example.meatballbackend.Dto.RespuestaDTO;
import org.example.meatballbackend.Entidades.Usuario;
import org.example.meatballbackend.Servicios.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private UsuarioService service;

    @PostMapping("/registro/perfil")
    public Usuario registro(@RequestBody RegistroDTO registroDTO){
        return service.registrarUsuario(registroDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<RespuestaDTO> registro(@RequestBody LoginDTO dto){
        return service.login(dto);
    }
}
