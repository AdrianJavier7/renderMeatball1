package org.example.meatballbackend.Entidades;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Chat", schema = "meatball" , catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class  Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @OneToOne
    @JoinColumn(name = "id_usuario1", referencedColumnName = "id")
    private Usuario usuario1;

    @OneToOne
    @JoinColumn(name = "id_usuario2", referencedColumnName = "id")
    private Usuario usuario2;

    @Column(name = "mensaje")
    private String mensaje;

}
