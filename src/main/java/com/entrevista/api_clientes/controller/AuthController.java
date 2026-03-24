package com.entrevista.api_clientes.controller;

import com.entrevista.api_clientes.model.Usuario;
import com.entrevista.api_clientes.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public String login(@RequestBody Usuario loginRequest) {
        return authService.iniciarLogin(loginRequest);
    }

    @PostMapping("/verificar")
    public String verificar(@RequestBody Usuario verifRequest) {
        return authService.validarMFA(verifRequest);
    }
}