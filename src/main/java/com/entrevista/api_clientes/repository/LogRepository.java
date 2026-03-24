package com.entrevista.api_clientes.repository;

import com.entrevista.api_clientes.model.LogAtividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<LogAtividade, Long> {
}
