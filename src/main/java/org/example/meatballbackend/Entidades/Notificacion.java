package org.example.meatballbackend.Entidades;


import jakarta.persistence.*;
import lombok.*;
import org.example.meatballbackend.Enums.TipoMensaje;

import java.time.LocalDateTime;

@Entity
@Table(name = "Notificacion", schema = "meatball" , catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_mensaje")
    private TipoMensaje tipoMensaje;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @OneToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    private Usuario usuario;

    @OneToOne
    @JoinColumn(name = "id_publicacion", referencedColumnName = "id")
    private Publicacion publicacion;

    @OneToOne
    @JoinColumn(name = "id_comentario", referencedColumnName = "id")
    private Comentario comentario;
}
