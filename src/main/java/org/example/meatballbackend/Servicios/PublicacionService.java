package org.example.meatballbackend.Servicios;

import org.example.meatballbackend.Repositorios.PublicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublicacionService {

    @Autowired
    private PublicacionRepository publicacionRepository;
}
