package org.example.meatballbackend.Servicios;

import org.example.meatballbackend.Repositorios.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService implements IChatService {

    @Autowired
    private ChatRepository chatRepository;
}
