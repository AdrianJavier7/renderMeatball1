package org.example.meatballbackend.Entidades;


import jakarta.persistence.*;
import lombok.*;
import org.example.meatballbackend.Enums.Dificultad;

import java.util.List;

@Entity
@Table(name = "Publicacion", schema = "meatball" , catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "imagen_link")
    private String imagenLink;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "receta")
    private String receta;

    @Enumerated(EnumType.STRING)
    @Column(name = "dificultad")
    private Dificultad dificultad;

    @Column(name = "tiempo_preparacion")
    private int tiempoPreparacion;

    @Column(name = "tiempo_coccion")
    private int tiempoCoccion;

    @Column(name = "raciones")
    private int raciones;

    @ManyToMany
    @JoinTable(
            name = "Publicacion_Ingrediente",
            schema = "meatball",
            catalog = "postgres",
            joinColumns = @JoinColumn(name = "publicacion_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "ingrediente_id", referencedColumnName = "id")
    )
    private List<Ingrediente> ingredientes;

    @ManyToMany
    @JoinTable(
            name = "Publicacion_Etiqueta",
            schema = "meatball",
            catalog = "postgres",
            joinColumns = @JoinColumn(name = "publicacion_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "Etiqueta_id", referencedColumnName = "id")
    )
    private List<Etiqueta> etiquetas;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, targetEntity = Usuario.class)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;


}
