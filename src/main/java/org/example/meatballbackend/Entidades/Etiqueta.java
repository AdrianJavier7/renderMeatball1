package org.example.meatballbackend.Entidades;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Etiqueta", schema = "meatball" , catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Etiqueta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @ManyToMany
    @JoinTable(
            name = "Publicacion_Etiqueta",
            joinColumns = @JoinColumn(name = "id_etiqueta", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_publicacion", referencedColumnName = "id")
    )
    private List<Publicacion> publicaciones;

}
