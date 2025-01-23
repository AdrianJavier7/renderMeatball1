package org.example.meatballbackend.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ChatDTO {
    private Integer idRemitente;
    private Integer idEmisor;
    private String mensaje;
}
