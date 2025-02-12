package org.example.meatballbackend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChaDBDTO {
    private Integer contacto;
    private LocalDateTime fecha;
    private String mensaje;
    private PerfilDTO perfilDTO;
}
