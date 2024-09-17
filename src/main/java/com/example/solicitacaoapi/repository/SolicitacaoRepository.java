package com.example.solicitacaoapi.repository;

import com.example.solicitacaoapi.model.SolicitacaoDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitacaoRepository extends JpaRepository<SolicitacaoDTO, Long> {
    List<SolicitacaoDTO> findByCnpj(String cnpj);
}
