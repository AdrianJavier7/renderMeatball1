package org.example.meatballbackend.Servicios;

import org.example.meatballbackend.Repositorios.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;
}
