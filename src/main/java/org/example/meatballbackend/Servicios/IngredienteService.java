package org.example.meatballbackend.Servicios;

import org.example.meatballbackend.Repositorios.IngredienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredienteService implements IIngredienteService {

    @Autowired
    private final IngredienteRepository ingredienteRepository;

    public IngredienteService(IngredienteRepository ingredienteRepository) {
        this.ingredienteRepository = ingredienteRepository;
    }

    public List<String> obtenerNombresIngredientes() {
        return ingredienteRepository.findAll()
                .stream()
                .map(ingrediente -> ingrediente.getNombre()) // Obtener solo el nombre
                .collect(Collectors.toList());
    }
}
