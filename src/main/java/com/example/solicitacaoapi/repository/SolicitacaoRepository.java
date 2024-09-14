package com.example.solicitacaoapi.repository;

import com.example.solicitacaoapi.model.SolicitacaoDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SolicitacaoRepository extends JpaRepository<SolicitacaoDTO, Long> {
    Optional<SolicitacaoDTO> findByCnpj(String cnpj);
}
