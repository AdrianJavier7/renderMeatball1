package org.example.meatballbackend.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.meatballbackend.Enums.Dificultad;
import org.example.meatballbackend.Enums.Estado;

@Data
@Getter
@Setter
public class PublicacionDTO {
    private Integer id;
    private String titulo;
    private String imagenLink;
    private String descripcion;
    private String receta;
    private Dificultad dificultad;
    private int tiempoPreparacion;
    private int tiempoCoccion;
    private int raciones;
    private Integer usuarioId;
    private String username;
}
