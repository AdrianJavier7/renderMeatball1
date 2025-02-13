package org.example.meatballbackend.Controladores;

import lombok.AllArgsConstructor;
import org.example.meatballbackend.Dto.LoginDTO;
import org.example.meatballbackend.Dto.RegistroDTO;
import org.example.meatballbackend.Dto.RespuestaDTO;
import org.example.meatballbackend.Entidades.Usuario;
import org.example.meatballbackend.Servicios.EmailService;
import org.example.meatballbackend.Servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private UsuarioService service;

    @Autowired
    private EmailService emailService;

    @PostMapping("/registro/perfil")
    public Usuario registro(@RequestBody RegistroDTO registroDTO){
        return service.registrarUsuario(registroDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<RespuestaDTO> registro(@RequestBody LoginDTO dto){
        return service.login(dto);
    }

    @PostMapping("/enviarEmail")
    public ResponseEntity<String> registerUser(@RequestBody Usuario user) {
        emailService.sendEmail(user.getEmail(), "¡Bienvenido/a a MeætBall! \uD83C\uDF7D\uFE0F\uD83D\uDC69\u200D\uD83C\uDF73", "" +
                "¡Qué alegría tenerte en nuestra comunidad! \uD83C\uDF89 MeætBall es el lugar perfecto para descubrir, compartir y disfrutar las mejores recetas, conectarte con amantes de la cocina y encontrar inspiración para cada comida.\n" +
                "\n" +
                "Aquí puedes:\n" +
                "\uD83E\uDD58 Explorar miles de recetas compartidas por otros usuarios.\n" +
                "\uD83D\uDCF8 Publicar tus propias creaciones culinarias y mostrar tu talento.\n" +
                "❤\uFE0F Guardar tus recetas favoritas y organizarlas fácilmente.\n" +
                "\uD83D\uDC69\u200D\uD83C\uDF73 Interactuar con otros apasionados de la cocina, dejando comentarios y valoraciones.\n" +
                "\n" +
                "Para comenzar, te recomendamos:\n" +
                "\uD83D\uDD39 Completar tu perfil para que la comunidad te conozca mejor.\n" +
                "\uD83D\uDD39 Seguir a otros cocineros para descubrir nuevas recetas.\n" +
                "\uD83D\uDD39 Subir tu primera receta y compartir tu pasión con todos.\n" +
                "\n" +
                "Si necesitas ayuda, estamos aquí para ti. No dudes en visitar nuestra sección de ayuda o escribirnos.\n" +
                "\n" +
                "¡Esperamos ver tus deliciosas creaciones pronto! \uD83C\uDF55\uD83E\uDD57\uD83C\uDF70\n" +
                "\n" +
                "Saludos,\n" +
                "El equipo de MeætBall \uD83D\uDC68\u200D\uD83C\uDF73\uD83D\uDD25");
        return ResponseEntity.ok("Usuario registrado y correo enviado");
    }
}
