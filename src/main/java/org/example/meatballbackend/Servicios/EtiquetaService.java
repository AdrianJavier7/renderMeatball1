package org.example.meatballbackend.Servicios;

import org.example.meatballbackend.Entidades.Etiqueta;
import org.example.meatballbackend.Repositorios.EtiquetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EtiquetaService implements IEtiquetaService {

    @Autowired
    private EtiquetaRepository etiquetaRepository;

    public List<Etiqueta> obtenerEtiquetas() {
        return etiquetaRepository.findAll();
    }
}
