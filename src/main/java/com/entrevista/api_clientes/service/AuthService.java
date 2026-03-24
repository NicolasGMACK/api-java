package com.entrevista.api_clientes.service;

import com.entrevista.api_clientes.model.Usuario;
import com.entrevista.api_clientes.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    public String iniciarLogin(Usuario loginRequest) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(loginRequest.getEmail());

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

    public String validarMFA(Usuario verifRequest) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(verifRequest.getEmail());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.getCodigoMFA() != null && usuario.getCodigoMFA().equals(verifRequest.getCodigoMFA())) {
                return Jwts.builder()
                        .setSubject(usuario.getEmail())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                        .signWith(SignatureAlgorithm.HS256, "MinhaChaveSecreta")
                        .compact();
            }
            return "Código inválido";
        }
        return "Usuário não encontrado";
    }
}