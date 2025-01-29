package org.example.meatballbackend.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.meatballbackend.Enums.Rol;

@Data
@Getter
@Setter
public class UsuarioDTO {
    private Integer id;
    private String nombre;
    private String email;
    private String contrasena;
    private Rol rol;
}
