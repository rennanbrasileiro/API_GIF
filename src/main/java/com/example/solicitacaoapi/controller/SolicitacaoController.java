package com.example.solicitacaoapi.controller;

import com.example.solicitacaoapi.model.RespostaDTO;
import com.example.solicitacaoapi.service.SolicitacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/solicitacao")
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    public SolicitacaoController(SolicitacaoService solicitacaoService) {
        this.solicitacaoService = solicitacaoService;
    }

    @PostMapping
    public ResponseEntity<RespostaDTO> receberSolicitacao(
            @RequestParam(required = true) String servicoSolicitacao,
            @RequestParam(required = true) String nomeSolicitante,
            @RequestParam(required = true) String emailSolicitante,
            @RequestParam(required = true) String cnpj,
            @RequestParam(required = true) String migracaoProdepeProind,
            @RequestParam(required = true) String naturezaProjeto,
            @RequestParam(required = true) String estabelecimento,
            @RequestParam(required = true) Integer quantidadeEmpregos,
            @RequestParam(required = true) Double valorInvestimentos,
            @RequestParam(required = true) MultipartFile arquivoPdf,
            @RequestParam(required = false) MultipartFile contratoSocial,
            @RequestParam(required = false) MultipartFile cnpjCartaoRfb,
            @RequestParam(required = false) MultipartFile certificadoFgts,
            @RequestParam(required = false) MultipartFile certidaoUniao,
            @RequestParam(required = false) MultipartFile certidaoSefazPe,
            @RequestParam(required = false) MultipartFile daeTfusp,
            @RequestParam(required = false) MultipartFile comprovanteDaeTfusp,
            @RequestParam(required = false) MultipartFile procuracao,
            @RequestParam(required = false) MultipartFile migracaoDecretos,
            @RequestParam(required = false) MultipartFile outros) {

        RespostaDTO resposta = solicitacaoService.processarSolicitacao(
                servicoSolicitacao, nomeSolicitante, emailSolicitante, cnpj,
                migracaoProdepeProind, naturezaProjeto, estabelecimento,
                quantidadeEmpregos, valorInvestimentos, arquivoPdf, contratoSocial,
                cnpjCartaoRfb, certificadoFgts, certidaoUniao, certidaoSefazPe,
                daeTfusp, comprovanteDaeTfusp, procuracao, migracaoDecretos, outros);

        if ("NOK".equals(resposta.getStatus())) {
            return ResponseEntity.badRequest().body(resposta);
        } else {
            return ResponseEntity.ok(resposta);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("Servidor está funcionando!");
    }
}
