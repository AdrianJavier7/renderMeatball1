package org.example.meatballbackend.Servicios;

import org.example.meatballbackend.Repositorios.EtiquetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EtiquetaService implements IEtiquetaService {

    @Autowired
    private EtiquetaRepository etiquetaRepository;
}
