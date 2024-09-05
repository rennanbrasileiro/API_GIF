package com.example.solicitacaoapi.repository;

import com.example.solicitacaoapi.model.RespostaDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RespostaRepository extends JpaRepository<RespostaDTO, Long> {
}
