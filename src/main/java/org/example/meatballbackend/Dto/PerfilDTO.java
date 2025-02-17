package org.example.meatballbackend.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.meatballbackend.Enums.Estado;

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
    private Estado estado;

    private String username;
}
