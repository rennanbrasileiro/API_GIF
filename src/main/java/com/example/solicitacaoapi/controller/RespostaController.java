package com.example.solicitacaoapi.controller;

import com.example.solicitacaoapi.model.RespostaDTO;
import com.example.solicitacaoapi.model.SolicitacaoDTO;
import com.example.solicitacaoapi.repository.RespostaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

        // Lista de mensagens de erro
        List<String> erros = new ArrayList<>();

        // Verificação dos campos obrigatórios
        if (solicitacao.getCnpj() == null || solicitacao.getCnpj().isEmpty()) {
            erros.add("CNPJ não cadastrado!");
        }
        if (solicitacao.getTipoAnexo() == null || solicitacao.getTipoAnexo().isEmpty()) {
            erros.add("Tipo de anexo obrigatório não enviado!");
        }
        if (solicitacao.getArquivoPdf() == null || solicitacao.getArquivoPdf().isEmpty()) {
            erros.add("Arquivo PDF obrigatório não enviado!");
        }

        // Se houver erros, retorna resposta de erro
        if (!erros.isEmpty()) {
            resposta.setStatus("NOK");
            resposta.setMensagemExcecao(String.join(" ", erros));
            respostaRepository.save(resposta);
            return ResponseEntity.badRequest().body(resposta);
        }

        // Se tudo estiver correto, retorna resposta de sucesso
        resposta.setStatus("OK");
        resposta.setNumeroProtocolo("2024PROTOCOL123");
        resposta.setAnexoResumo("Link para o PDF de Resumo");
        resposta.setSituacaoSolicitacao("Em Andamento");

        respostaRepository.save(resposta);
        return ResponseEntity.ok(resposta);
    }
}
