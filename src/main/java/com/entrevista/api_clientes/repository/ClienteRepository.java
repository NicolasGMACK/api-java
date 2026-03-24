package com.entrevista.api_clientes.repository;

import com.entrevista.api_clientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // aqui já temos save(), findAll(), findById(), deleteById()
}
