package org.example.meatballbackend.Entidades;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Comentario", schema = "meatball" , catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "comentario")
    private String comentario;

    @Column(name = "fecha")
    private String fecha;

    @OneToOne
    @JoinColumn(name = "id_perfil", referencedColumnName = "id")
    private Perfil perfil;

    @OneToOne
    @JoinColumn(name = "id_publicacion", referencedColumnName = "id")
    private Publicacion publicacion;
}
