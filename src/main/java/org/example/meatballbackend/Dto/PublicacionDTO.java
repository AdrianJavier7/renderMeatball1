package org.example.meatballbackend.Dto;

import lombok.*;
import org.example.meatballbackend.Enums.Dificultad;
import org.example.meatballbackend.Enums.Estado;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    private Integer perfilId;
    private String username;
    private List<IngredienteDTO> ingredientes = new ArrayList<>();
    private List<EtiquetaDTO> etiquetas = new ArrayList<>();
    private String fotoPerfilLink;
}
