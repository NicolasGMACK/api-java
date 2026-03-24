package com.entrevista.api_clientes.controller;

import com.entrevista.api_clientes.model.Cliente;
import com.entrevista.api_clientes.model.LogAtividade;
import com.entrevista.api_clientes.repository.ClienteRepository;
import com.entrevista.api_clientes.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/clientes")
@CrossOrigin("*")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private LogRepository logRepository;

    @GetMapping
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    @PostMapping
    public Cliente salvar(@RequestBody Cliente cliente) {
        Cliente novoCliente = clienteRepository.save(cliente);

        LogAtividade log = new LogAtividade();
        log.setAcao("Cadastrou o cliente: " + novoCliente.getNome());
        log.setDataHora(LocalDateTime.now());
        logRepository.save(log);

        return novoCliente;
    }

}
