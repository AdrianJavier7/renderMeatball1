package org.example.meatballbackend.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class ComentarioDTO {
    private String comentario;
    private String fecha;
    private Integer idPublicacion;

    private String nombreUsuario;
    private String fotoUsuario;

}
