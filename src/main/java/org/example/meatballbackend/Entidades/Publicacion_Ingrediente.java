package org.example.meatballbackend.Entidades;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "Publicacion_Ingrediente", schema = "meatball" , catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Publicacion_Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "cantidad")
    private String cantidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ingrediente", referencedColumnName = "id")
    private Ingrediente ingrediente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_publicacion", referencedColumnName = "id")
    private Publicacion publicacion;


}
