package org.example.meatballbackend.Controladores;

import org.example.meatballbackend.Servicios.IngredienteService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ingredientes")
@CrossOrigin(origins = "http://localhost:4200") // Asegúrate de permitir Angular
public class IngredienteController {

    private final IngredienteService ingredienteService;

    public IngredienteController(IngredienteService ingredienteService) {
        this.ingredienteService = ingredienteService;
    }

    @GetMapping("/buscar")
    public List<String> obtenerIngredientes() {
        return ingredienteService.obtenerNombresIngredientes();
    }
}
