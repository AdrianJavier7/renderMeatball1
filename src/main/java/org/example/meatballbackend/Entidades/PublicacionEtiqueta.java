package org.example.meatballbackend.Entidades;

import jakarta.persistence.*;
import lombok.*;
import org.example.meatballbackend.Enums.TipoCantidad;

@Entity
@Table(name = "Publicacion_Etiqueta", schema = "meatball", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PublicacionEtiqueta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "publicacion_id", referencedColumnName = "id")
    private Publicacion publicacion;

    @ManyToOne
    @JoinColumn(name = "etiqueta_id", referencedColumnName = "id")
    private Etiqueta etiqueta;
}

