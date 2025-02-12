package org.example.meatballbackend.Repositorios;

import org.example.meatballbackend.Entidades.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Query(value = "SELECT DISTINCT " +
            "CASE " +
            "    WHEN c.id_usuario1 = :id THEN c.id_usuario2 " +
            "    ELSE c.id_usuario1 " +
            "END " +
            "FROM meatball.Chat c " +
            "WHERE c.id_usuario1 = :id OR c.id_usuario2 = :id",
            nativeQuery = true)
    List<Integer> getConversacionesActivas(Integer id);


    @Query(value = "SELECT c FROM Chat c " +
            "WHERE (c.usuario1.id = :idPerfil1 AND c.usuario2.id = :idPerfil2) " +
            "   OR (c.usuario1.id = :idPerfil2 AND c.usuario2.id = :idPerfil1) " +
            "ORDER BY c.fecha DESC LIMIT 1")
    Chat getUltimoMensaje(Integer idPerfil1, Integer idPerfil2);



    @Query(value = "SELECT * FROM ( " +
            "    SELECT DISTINCT ON (contacto) " +
            "        CASE " +
            "            WHEN c.id_usuario1 = :idPerfil THEN c.id_usuario2 " +
            "            ELSE c.id_usuario1 " +
            "        END AS contacto, " +
            "        c.fecha, " +
            "        c.mensaje " +
            "    FROM meatball.Chat c " +
            "    WHERE c.id_usuario1 = :idPerfil OR c.id_usuario2 = :idPerfil " +
            "    ORDER BY contacto, c.fecha DESC " +
            ") sub " +
            "ORDER BY fecha DESC",
            nativeQuery = true)
    List<Object> getConversaciones(Integer idPerfil);


    @Query("SELECT c FROM Chat c " +
            "WHERE (c.usuario1.id = :idPerfil1 AND c.usuario2.id = :idPerfil2) " +
            "   OR (c.usuario1.id = :idPerfil2 AND c.usuario2.id = :idPerfil1) " +
            "ORDER BY c.fecha ASC")
    List<Chat> getByEmisorYReceptor(Integer idPerfil1, Integer idPerfil2);
}
