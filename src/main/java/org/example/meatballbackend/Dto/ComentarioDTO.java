package org.example.meatballbackend.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ComentarioDTO {
    private Integer id;
    private String comentario;
}
