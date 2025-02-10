package org.example.meatballbackend.Servicios;

import org.example.meatballbackend.Dto.PerfilDTO;
import org.example.meatballbackend.Entidades.Perfil;

import java.util.List;

public interface IPerfilService {
    PerfilDTO updatePerfil(Perfil perfilLogueado, PerfilDTO perfilDTO);
}