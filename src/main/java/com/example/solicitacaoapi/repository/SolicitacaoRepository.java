package com.example.solicitacaoapi.repository;

import com.example.solicitacaoapi.model.SolicitacaoDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitacaoRepository extends JpaRepository<SolicitacaoDTO, Long> {
}
