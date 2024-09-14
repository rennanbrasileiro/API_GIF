package com.example.solicitacaoapi.controller;

import com.example.solicitacaoapi.model.SolicitacaoDTO;
import com.example.solicitacaoapi.model.RespostaDTO;
import com.example.solicitacaoapi.repository.RespostaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resposta")
public class RespostaController {

    private final RespostaRepository respostaRepository;

    public RespostaController(RespostaRepository respostaRepository) {
        this.respostaRepository = respostaRepository;
    }

    @PostMapping
    public ResponseEntity<RespostaDTO> processarResposta(@RequestBody SolicitacaoDTO solicitacao) {
        RespostaDTO resposta = new RespostaDTO();
        // Implementar lógica para processamento de resposta
        // Retornar resposta apropriada
        resposta.setStatus("OK");
        resposta.setNumeroProtocolo("2024PROTOCOL123");
        resposta.setAnexoResumo("Link para o PDF de Resumo");
        resposta.setSituacaoSolicitacao("Em Andamento");
        respostaRepository.save(resposta);
        return ResponseEntity.ok(resposta);
    }
}
