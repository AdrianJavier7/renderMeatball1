package org.example.meatballbackend.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class IngredienteDTO {
    private Integer id;
    private String nombre;
    private Integer cantidad;
}
