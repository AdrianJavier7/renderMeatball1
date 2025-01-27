package org.example.meatballbackend.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PerfilDTO {
    private Integer id;
    private String nombre;
    private String apellidos;
    private String fotoPerfilLink;
    private String email;
    private String telefono;
    private String estado;

    private String username;
}