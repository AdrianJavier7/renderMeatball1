package org.example.meatballbackend.Entidades;

import jakarta.persistence.*;
import lombok.*;
import org.example.meatballbackend.Enums.Rol;

@Entity
@Table(name = "Usuario", schema = "meatball" , catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "contrasena")
    private String contrasena;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol")
    private Rol rol;


}
