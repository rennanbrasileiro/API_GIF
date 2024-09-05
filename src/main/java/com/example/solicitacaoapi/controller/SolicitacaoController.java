package com.example.solicitacaoapi.controller;

import com.example.solicitacaoapi.model.SolicitacaoDTO;
import com.example.solicitacaoapi.model.RespostaDTO;
import com.example.solicitacaoapi.repository.SolicitacaoRepository;
import com.example.solicitacaoapi.repository.RespostaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solicitacao")
public class SolicitacaoController {

    private final SolicitacaoRepository solicitacaoRepository;
    private final RespostaRepository respostaRepository;

    public SolicitacaoController(SolicitacaoRepository solicitacaoRepository, RespostaRepository respostaRepository) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.respostaRepository = respostaRepository;
    }

    @PostMapping
    public ResponseEntity<RespostaDTO> receberSolicitacao(@RequestBody SolicitacaoDTO solicitacao) {
        RespostaDTO resposta = new RespostaDTO();

        // Simulando validações e lógica de negócio
        if (solicitacao.getCnpj().equals("00.000.000/0001-91")) {
            resposta.setStatus("NOK");
            resposta.setMensagemExcecao("CNPJ não cadastrado!");
            respostaRepository.save(resposta);
            return ResponseEntity.badRequest().body(resposta);
        }

        // Validação dos Anexos
        if (solicitacao.getTipoAnexo() == null || solicitacao.getArquivoPdf() == null) {
            resposta.setStatus("NOK");
            resposta.setMensagemExcecao("Anexo obrigatório não enviado!");
            respostaRepository.save(resposta);
            return ResponseEntity.badRequest().body(resposta);
        }

        // Persistindo a solicitação
        solicitacaoRepository.save(solicitacao);

        // Lógica para criar um número de protocolo (mocado)
        resposta.setStatus("OK");
        resposta.setNumeroProtocolo("2024PROTOCOL123");
        resposta.setAnexoResumo("Link para o PDF de Resumo");
        resposta.setSituacaoSolicitacao("Em Andamento");

        respostaRepository.save(resposta);

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("Servidor está funcionando!");
    }
}
