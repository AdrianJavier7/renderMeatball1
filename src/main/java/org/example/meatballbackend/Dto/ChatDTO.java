package org.example.meatballbackend.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ChatDTO {
    private Integer id;
    private String mensaje;
}
