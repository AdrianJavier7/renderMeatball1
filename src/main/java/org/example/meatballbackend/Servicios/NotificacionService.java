package org.example.meatballbackend.Servicios;

import org.example.meatballbackend.Repositorios.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService implements IChatService {

    @Autowired
    private NotificacionRepository notificacionRepository;
}
