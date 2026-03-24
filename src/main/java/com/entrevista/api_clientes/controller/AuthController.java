package com.entrevista.api_clientes.controller;

import com.entrevista.api_clientes.model.Usuario;
import com.entrevista.api_clientes.repository.UsuarioRepository;
import com.entrevista.api_clientes.service.EmailService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping
    public String login(@RequestBody Usuario loginRequest) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(loginRequest.getEmail());

        if (usuarioOpt.isPresent() && usuarioOpt.get().getSenha().equals(loginRequest.getSenha())) {
            String codigo = String.format("%06d", new Random().nextInt(999999));

            Usuario usuario = usuarioOpt.get();
            usuario.setCodigoMFA(codigo);
            usuarioRepository.save(usuario);

            emailService.enviarEmail(usuario.getEmail(), "Seu código de acesso", "Código: " + codigo);

            return "Código enviado por e-mail.";
        }
        return "Usuário ou senha inválidos.";
    }

    @PostMapping("/verificar")
    public String verificar(@RequestBody Usuario verifRequest) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(verifRequest.getEmail());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.getCodigoMFA().equals(verifRequest.getCodigoMFA())) {
                String token = Jwts.builder()
                        .setSubject(usuario.getEmail())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                        .signWith(SignatureAlgorithm.HS256, "MinhaChaveSecreta")
                        .compact();

                return token;
            }
            return "Código inválido";
        }
    }
}

