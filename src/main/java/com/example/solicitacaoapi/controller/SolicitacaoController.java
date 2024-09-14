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
        
        // Verificação dos campos obrigatórios
        StringBuilder mensagemExcecao = new StringBuilder();
        if (solicitacao.getServicoSolicitacao() == null || solicitacao.getServicoSolicitacao().isEmpty()) {
            mensagemExcecao.append("Serviço da Solicitação obrigatório não enviado! ");
        }
        if (solicitacao.getNomeSolicitante() == null || solicitacao.getNomeSolicitante().isEmpty()) {
            mensagemExcecao.append("Nome do Solicitante obrigatório não enviado! ");
        }
        if (solicitacao.getEmailSolicitante() == null || solicitacao.getEmailSolicitante().isEmpty()) {
            mensagemExcecao.append("E-mail do Solicitante obrigatório não enviado! ");
        }
        if (solicitacao.getCnpj() == null || solicitacao.getCnpj().isEmpty()) {
            mensagemExcecao.append("CNPJ obrigatório não enviado! ");
        } else {
            // Verificando se o CNPJ está cadastrado na base de dados
            boolean cnpjCadastrado = solicitacaoRepository.findByCnpj(solicitacao.getCnpj()).isPresent();
            if (!cnpjCadastrado) {
                mensagemExcecao.append("CNPJ não cadastrado! ");
            }
        }
        if (solicitacao.getTipoAnexo() == null) {
            mensagemExcecao.append("Tipo de anexo obrigatório não enviado! ");
        }
        if (solicitacao.getArquivoPdf() == null || solicitacao.getArquivoPdf().isEmpty()) {
            mensagemExcecao.append("Arquivo PDF obrigatório não enviado!");
        }

        if (mensagemExcecao.length() > 0) {
            resposta.setStatus("NOK");
            resposta.setMensagemExcecao(mensagemExcecao.toString());
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
