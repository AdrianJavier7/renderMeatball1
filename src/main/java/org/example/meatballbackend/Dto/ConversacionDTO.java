package org.example.meatballbackend.Dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversacionDTO {

    private String nombrePerfil;
    private String fotoPerfil;
    private String ultimoMensaje;
    private LocalDateTime fechaUltimoMensaje;

}
