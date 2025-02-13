package org.example.meatballbackend.Entidades;

import jakarta.persistence.*;
import lombok.*;
import org.example.meatballbackend.Enums.TipoCantidad;

@Entity
@Table(name = "Publicacion_Ingrediente", schema = "meatball", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PublicacionIngrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "cantidad")
    private int cantidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cantidad")
    private TipoCantidad tipoCantidad;

    @ManyToOne
    @JoinColumn(name = "publicacion_id", referencedColumnName = "id")
    private Publicacion publicacion;

    @ManyToOne
    @JoinColumn(name = "ingrediente_id", referencedColumnName = "id")
    private Ingrediente ingrediente;
}
