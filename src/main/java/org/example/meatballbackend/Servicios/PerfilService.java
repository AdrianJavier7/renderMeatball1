package org.example.meatballbackend.Servicios;

import org.example.meatballbackend.Repositorios.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerfilService {

    @Autowired
    private PerfilRepository perfilRepository;
}
