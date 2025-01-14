package org.example.meatballbackend.Servicios;

import org.example.meatballbackend.Repositorios.IngredienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngredienteService {

    @Autowired
    private IngredienteRepository ingredienteRepository;
}
