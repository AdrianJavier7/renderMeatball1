package org.example.meatballbackend.Servicios;

import org.example.meatballbackend.Dto.LoginDTO;
import org.example.meatballbackend.Dto.RegistroDTO;
import org.example.meatballbackend.Dto.RespuestaDTO;
import org.example.meatballbackend.Entidades.Perfil;
import org.example.meatballbackend.Entidades.Usuario;
import org.example.meatballbackend.Enums.Estado;
import org.example.meatballbackend.Enums.Rol;
import org.example.meatballbackend.Repositorios.UsuarioRepository;
import org.example.meatballbackend.Security.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PerfilService perfilService;

    @Autowired
    private JWTService jwtService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findTopByUsername(username).orElse(null);
    }

    public Usuario registrarUsuario(RegistroDTO dto){
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(dto.getUsername());
        nuevoUsuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        nuevoUsuario.setRol(Rol.Perfil);
        nuevoUsuario.setEstado(Estado.Activo);

        Perfil perfil = new Perfil();
        perfil.setNombre(dto.getNombre());
        perfil.setApellidos(dto.getApellidos());
        perfil.setFotoPerfilLink(dto.getFotoPerfilLink());
        perfil.setEmail(dto.getEmail());
        perfil.setTelefono(dto.getTelefono());
        perfil.setEstado(dto.getEstado());

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        perfil.setUsuario(usuarioGuardado);
        Perfil perfilGuardado = perfilService.guardarPerfil(perfil);

        return usuarioGuardado;
    }

    public ResponseEntity<RespuestaDTO> login(LoginDTO dto) {
        Optional<Usuario> usuarioOpcional = usuarioRepository.findTopByUsername(dto.getUsername());

        if (usuarioOpcional.isPresent()) {
            Usuario usuario = usuarioOpcional.get();

            if (passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {

                  String token = jwtService.generateToken(usuario);
                return ResponseEntity
                        .ok(RespuestaDTO
                                .builder()
                                .estado(HttpStatus.OK.value())
                                .token(token).build());
            } else {
                throw new BadCredentialsException("Contraseña incorrecta");
            }
        } else {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }
}
