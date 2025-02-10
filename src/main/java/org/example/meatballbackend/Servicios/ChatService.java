package org.example.meatballbackend.Servicios;

import org.example.meatballbackend.Dto.ChatDTO;
import org.example.meatballbackend.Entidades.Chat;
import org.example.meatballbackend.Entidades.Usuario;
import org.example.meatballbackend.Repositorios.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatService implements IChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UsuarioService usuarioService;

    public Chat guardarMensaje(ChatDTO chatDTO) {

        Usuario receptor = usuarioService.obtenerUsuarioPorId(chatDTO.getIdReceptor());
        Usuario emisor = usuarioService.obtenerUsuarioPorId(chatDTO.getIdEmisor());

        Chat chat = new Chat();
        chat.setUsuario1(emisor);
        chat.setUsuario2(receptor);
        chat.setMensaje(chatDTO.getMensaje());
        chat.setFecha(LocalDateTime.now());
        chatRepository.save(chat);
        
        return chat;
    }
}
