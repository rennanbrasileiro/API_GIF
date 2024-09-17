package com.example.solicitacaoapi.controller;

import com.example.solicitacaoapi.model.RespostaDTO;
import com.example.solicitacaoapi.service.SolicitacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/solicitacao")
public class SolicitacaoController {

    private static final Logger logger = LoggerFactory.getLogger(SolicitacaoController.class);

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

        // Adicionando logs para verificar se o arquivo foi recebido
        logFileInfo(arquivoPdf, "arquivoPdf");

        // Continue com o processamento normal
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

    private void logFileInfo(MultipartFile file, String fileName) {
        if (file == null || file.isEmpty()) {
            logger.error("O arquivo '{}' não foi recebido ou está vazio.", fileName);
        } else {
            logger.info("Arquivo '{}' recebido com sucesso.", fileName);
            logger.info("Nome do arquivo: {}", file.getOriginalFilename());
            logger.info("Tamanho do arquivo: {} bytes", file.getSize());
            logger.info("Tipo de conteúdo: {}", file.getContentType());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        String mensagem = """
            <html>
                <body style="font-family: Arial, sans-serif; text-align: center; background-color: #f4f4f9; padding: 50px;">
                    <div style="display: inline-block; background-color: #6c757d; color: #fff; padding: 20px; border-radius: 10px;">
                        <h1>📡 Servidor Ativo!</h1>
                        <p>O servidor está funcionando corretamente e pronto para processar solicitações.</p>
                        <p>Data e Hora Atual: <strong>%s</strong></p>
                        <p>Entre em contato caso precise de ajuda adicional!</p>
                    </div>
                </body>
            </html>
        """;
        String dataHoraAtual = java.time.LocalDateTime.now().toString();

        return ResponseEntity.ok(String.format(mensagem, dataHoraAtual));
    }
}
