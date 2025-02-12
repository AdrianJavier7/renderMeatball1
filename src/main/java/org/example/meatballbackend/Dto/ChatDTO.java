package org.example.meatballbackend.Dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {
    private Integer id;
    private String mensaje;
    private Integer idEmisor;
    private Integer idReceptor;
    private String fecha;
}
