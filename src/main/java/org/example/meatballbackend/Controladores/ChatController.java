package org.example.meatballbackend.Controladores;

import org.example.meatballbackend.Dto.*;
import org.example.meatballbackend.Entidades.Perfil;
import org.example.meatballbackend.Security.JWTService;
import org.example.meatballbackend.Servicios.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService service;
    @Autowired
    private JWTService jwtService;


    @GetMapping("/conversaciones")
    public List<ConversacionDTO> getConversacionesByperfilId(@RequestParam Integer idPerfil){
        return service.getConversacionesByIdPerfil(idPerfil);
    }

    @GetMapping("/chats")
    public List<ChaDBDTO> getConversacionesByperfil(@RequestHeader("Authorization") String token){
        Perfil perfil = jwtService.extraerPerfilToken(token);
        return service.getConversaciones(perfil);
    }

    @GetMapping("/{idContacto}")
    public List<ChatDTO> getConversacionesByperfil(@RequestHeader("Authorization") String token,
                                                   @PathVariable Integer idContacto){
        Perfil perfil = jwtService.extraerPerfilToken(token);
        return service.getByEmisorReceptor(perfil.getId(), idContacto);
    }

    @PostMapping("/enviar")
    public RespuestaDTO enviarMensaje(@RequestHeader("Authorization") String token, @RequestBody EnviarMensajeDTO enviarMensajeDTO ){
        Perfil perfil = jwtService.extraerPerfilToken(token);
        return service.enviarMensaje(perfil, enviarMensajeDTO);
    }
}
