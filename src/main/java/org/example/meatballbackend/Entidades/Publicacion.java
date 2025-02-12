package org.example.meatballbackend.Entidades;


import jakarta.persistence.*;
import lombok.*;
import org.example.meatballbackend.Enums.Dificultad;
import org.example.meatballbackend.Enums.Estado;
import org.example.meatballbackend.Enums.TipoCantidad;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private Estado estado;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, targetEntity = Usuario.class)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, targetEntity = Perfil.class)
    @JoinColumn(name = "perfil_id", referencedColumnName = "id")
    private Perfil perfil;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Usuario_Like"
            )
    private List<Usuario> likes;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Etiqueta_Publicacion",
            joinColumns = @JoinColumn(name = "publicacion_id"),
            inverseJoinColumns = @JoinColumn(name = "etiqueta_id")
    )
    private List<Etiqueta> etiquetas;



}
