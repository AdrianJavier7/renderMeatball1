package org.example.meatballbackend.Entidades;

import jakarta.persistence.*;
import lombok.*;

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

    @OneToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    private Usuario usuario;

    @OneToOne
    @JoinColumn(name = "id_publicacion", referencedColumnName = "id")
    private Publicacion publicacion;


}
