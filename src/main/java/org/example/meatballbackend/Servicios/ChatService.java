package org.example.meatballbackend.Servicios;

import org.example.meatballbackend.Dto.*;
import org.example.meatballbackend.Entidades.Chat;
import org.example.meatballbackend.Entidades.Perfil;
import org.example.meatballbackend.Entidades.Usuario;
import org.example.meatballbackend.Repositorios.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;

@Service
public class ChatService implements IChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private PerfilService perfilService;

    public List<ConversacionDTO> getConversacionesByIdPerfil(Integer idPerfil){


        List<Integer> perfilesConversaciones = chatRepository.getConversacionesActivas(idPerfil);
        List<ConversacionDTO> conversacionDTOS = new ArrayList<>();

        for(Integer id: perfilesConversaciones) {

            Perfil p = perfilService.buscarPorId(id);
            Chat ultimoMensaje = chatRepository.getUltimoMensaje(idPerfil,p.getId());

            ConversacionDTO dto = new ConversacionDTO();
            dto.setNombrePerfil(p.getNombre().concat(" ").concat(p.getApellidos()) );
            dto.setFotoPerfil(p.getFotoPerfilLink());
            dto.setUltimoMensaje(ultimoMensaje.getMensaje());
            dto.setFechaUltimoMensaje(ultimoMensaje.getFecha());
            conversacionDTOS.add(dto);
        }


        return conversacionDTOS;
    }

    public List<ChaDBDTO> getConversaciones(Perfil perfil){
        List<Object> cosas =  chatRepository.getConversaciones(perfil.getId());
        List<ChaDBDTO> chats = new ArrayList<>();
        for(Object o : cosas) {
            Object[] oa = (Object[]) o;
            Timestamp tm = (Timestamp) oa[1];
            PerfilDTO perfilDTO = perfilService.getPerfilById((Integer) oa[0]);
            chats.add(new ChaDBDTO((Integer) oa[0],tm.toLocalDateTime() , (String) oa[2],perfilDTO));
        }

        return chats;
    }

    public List<ChatDTO> getByEmisorReceptor(Integer idPerfil1, Integer idPerfil2){

        List<ChatDTO> dtos = new ArrayList<>();
        chatRepository.getByEmisorYReceptor(idPerfil1, idPerfil2).stream().forEach(
                m-> dtos.add(new ChatDTO(m.getId(), m.getMensaje(),
                        m.getUsuario1().getId(), m.getUsuario2().getId(), m.getFecha().toString()))
        );


        return dtos;
    }

    public RespuestaDTO enviarMensaje(Perfil perfil, EnviarMensajeDTO enviarMensajeDTO) {


        Chat chat = new Chat();
        chat.setFecha(LocalDateTime.now());
        chat.setUsuario1(perfil);
        chat.setUsuario2(perfilService.buscarPorId(enviarMensajeDTO.getIdReceptor()));
        chat.setMensaje(enviarMensajeDTO.getTexto());

        chatRepository.save(chat);

        return RespuestaDTO.builder().estado(200).mensaje("Mensaje Enviado").build();
    }
}
