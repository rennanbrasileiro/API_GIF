package com.example.solicitacaoapi.controller;

import com.example.solicitacaoapi.model.RespostaDTO;
import com.example.solicitacaoapi.service.SolicitacaoService;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/solicitacao")
public class SolicitacaoController {

    private static final Logger logger = LoggerFactory.getLogger(SolicitacaoController.class);
    private final SolicitacaoService solicitacaoService;
    private final String baseUrl = "http://localhost:8080/api/solicitacao/pdf/";

    public SolicitacaoController(SolicitacaoService solicitacaoService) {
        this.solicitacaoService = solicitacaoService;
    }

    @PostMapping
    public ResponseEntity<RespostaDTO> receberSolicitacao(
            @RequestParam String servicoSolicitacao,
            @RequestParam String nomeSolicitante,
            @RequestParam String telefoneSolicitante,
            @RequestParam String emailSolicitante,
            @RequestParam String cnpj,
            @RequestParam String migracaoProdepeProind,
            @RequestParam String naturezaProjeto,
            @RequestParam String estabelecimento,
            @RequestParam Integer quantidadeEmpregos,
            @RequestParam Double valorInvestimentos,
            @RequestParam String dataConstituicao, // Alterado para String
            @RequestParam MultipartFile arquivoPdf,
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

        // Validação do campo dataConstituicao
        if (dataConstituicao == null || dataConstituicao.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new RespostaDTO("NOK", "O campo data de constituição é obrigatório."));
        }

        // Logando informações dos arquivos para garantir que foram recebidos
        logFileInfo(arquivoPdf, "arquivoPdf");

        // Continue com o processamento normal
        RespostaDTO resposta = solicitacaoService.processarSolicitacao(
                servicoSolicitacao, nomeSolicitante, telefoneSolicitante, emailSolicitante, cnpj,
                migracaoProdepeProind, naturezaProjeto, estabelecimento,
                quantidadeEmpregos, valorInvestimentos, dataConstituicao, arquivoPdf, contratoSocial,
                cnpjCartaoRfb, certificadoFgts, certidaoUniao, certidaoSefazPe,
                daeTfusp, comprovanteDaeTfusp, procuracao, migracaoDecretos, outros);

        // Verificando o status da resposta
        if ("NOK".equals(resposta.getStatus())) {
            return ResponseEntity.badRequest().body(resposta);
        }

        // Geração do PDF com os parâmetros da solicitação
        String protocolo = resposta.getNumeroProtocolo();
        String pdfLink = baseUrl + protocolo;

        // Salvar PDF em um diretório acessível para download
        String pdfPath = "pdf/" + protocolo + ".pdf";
        byte[] pdfBytes = gerarPdf(
                servicoSolicitacao, nomeSolicitante, telefoneSolicitante, emailSolicitante, cnpj, migracaoProdepeProind,
                naturezaProjeto, estabelecimento, quantidadeEmpregos, valorInvestimentos,
                contratoSocial != null ? contratoSocial.getOriginalFilename() : "Não fornecido",
                cnpjCartaoRfb != null ? cnpjCartaoRfb.getOriginalFilename() : "Não fornecido",
                certificadoFgts != null ? certificadoFgts.getOriginalFilename() : "Não fornecido",
                certidaoUniao != null ? certidaoUniao.getOriginalFilename() : "Não fornecido",
                certidaoSefazPe != null ? certidaoSefazPe.getOriginalFilename() : "Não fornecido",
                daeTfusp != null ? daeTfusp.getOriginalFilename() : "Não fornecido",
                comprovanteDaeTfusp != null ? comprovanteDaeTfusp.getOriginalFilename() : "Não fornecido",
                procuracao != null ? procuracao.getOriginalFilename() : "Não fornecido",
                migracaoDecretos != null ? migracaoDecretos.getOriginalFilename() : "Não fornecido",
                outros != null ? outros.getOriginalFilename() : "Não fornecido");

        try {
            Files.write(Paths.get(pdfPath), pdfBytes);
        } catch (Exception e) {
            logger.error("Erro ao salvar o PDF", e);
        }

        resposta.setAnexoResumo(pdfLink);

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/pdf/{numeroProtocolo}")
    public ResponseEntity<byte[]> getPdf(@PathVariable String numeroProtocolo) {
        String pdfPath = "pdf/" + numeroProtocolo + ".pdf";
        try {
            File file = new File(pdfPath);
            byte[] content = Files.readAllBytes(file.toPath());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", file.getName());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(content);
        } catch (Exception e) {
            logger.error("Erro ao ler o PDF", e);
            return ResponseEntity.notFound().build();
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

    // Método para gerar o PDF a partir dos parâmetros da solicitação
    private byte[] gerarPdf(String servicoSolicitacao, String nomeSolicitante, String telefoneSolicitante, String emailSolicitante, String cnpj,
                            String migracaoProdepeProind, String naturezaProjeto, String estabelecimento,
                            Integer quantidadeEmpregos, Double valorInvestimentos,
                            String contratoSocial, String cnpjCartaoRfb, String certificadoFgts,
                            String certidaoUniao, String certidaoSefazPe, String daeTfusp,
                            String comprovanteDaeTfusp, String procuracao, String migracaoDecretos, String outros) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(byteArrayOutputStream);
            com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Adicionando conteúdo ao PDF
            document.add(new Paragraph("Dados da Solicitação"));
            document.add(new Paragraph("Serviço Solicitado: " + servicoSolicitacao));
            document.add(new Paragraph("Nome do Solicitante: " + nomeSolicitante));
            document.add(new Paragraph("Telefone do Solicitante: " + telefoneSolicitante));
            document.add(new Paragraph("E-mail do Solicitante: " + emailSolicitante));
            document.add(new Paragraph("CNPJ: " + cnpj));
            document.add(new Paragraph("Migração Prodepe Proind: " + migracaoProdepeProind));
            document.add(new Paragraph("Natureza do Projeto: " + naturezaProjeto));
            document.add(new Paragraph("Estabelecimento: " + estabelecimento));
            document.add(new Paragraph("Quantidade de Empregos: " + quantidadeEmpregos));
            document.add(new Paragraph("Valor dos Investimentos: " + valorInvestimentos));
            document.add(new Paragraph("Contrato Social: " + contratoSocial));
            document.add(new Paragraph("CNPJ Cartão RFB: " + cnpjCartaoRfb));
            document.add(new Paragraph("Certificado FGTS: " + certificadoFgts));
            document.add(new Paragraph("Certidão de União: " + certidaoUniao));
            document.add(new Paragraph("Certidão SEFaz PE: " + certidaoSefazPe));
            document.add(new Paragraph("DAE Tfusp: " + daeTfusp));
            document.add(new Paragraph("Comprovante DAE Tfusp: " + comprovanteDaeTfusp));
            document.add(new Paragraph("Procuração: " + procuracao));
            document.add(new Paragraph("Migração Decretos: " + migracaoDecretos));
            document.add(new Paragraph("Outros: " + outros));

            document.close();

            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            logger.error("Erro ao gerar o PDF", e);
            return new byte[0];
        }
    }

    // Método para logar informações dos arquivos recebidos
    private void logFileInfo(MultipartFile file, String fileName) {
        if (file != null) {
            logger.info("Arquivo {} recebido: Nome = {}, Tamanho = {} bytes", fileName, file.getOriginalFilename(), file.getSize());
        } else {
            logger.info("Arquivo {} não recebido", fileName);
        }
    }
}
