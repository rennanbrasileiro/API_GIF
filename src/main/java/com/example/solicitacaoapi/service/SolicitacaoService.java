package com.example.solicitacaoapi.service;

import com.example.solicitacaoapi.model.SolicitacaoDTO;
import com.example.solicitacaoapi.model.RespostaDTO;
import com.example.solicitacaoapi.repository.SolicitacaoRepository;
import com.example.solicitacaoapi.repository.RespostaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final RespostaRepository respostaRepository;

    public SolicitacaoService(SolicitacaoRepository solicitacaoRepository, RespostaRepository respostaRepository) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.respostaRepository = respostaRepository;
    }

    public RespostaDTO processarSolicitacao(String servicoSolicitacao, String nomeSolicitante, String telefoneSolicitante,
                                             String emailSolicitante, String cnpj, String migracaoProdepeProind,
                                             String naturezaProjeto, String estabelecimento, Integer quantidadeEmpregos,
                                             Double valorInvestimentos, LocalDate dataConstituicao, MultipartFile arquivoPdf,
                                             MultipartFile contratoSocial, MultipartFile cnpjCartaoRfb,
                                             MultipartFile certificadoFgts, MultipartFile certidaoUniao,
                                             MultipartFile certidaoSefazPe, MultipartFile daeTfusp,
                                             MultipartFile comprovanteDaeTfusp, MultipartFile procuracao,
                                             MultipartFile migracaoDecretos, MultipartFile outros) {

        RespostaDTO resposta = new RespostaDTO();
        StringBuilder mensagemExcecao = new StringBuilder();

        // Validação dos campos obrigatórios
        validarCamposObrigatorios(servicoSolicitacao, nomeSolicitante, telefoneSolicitante, emailSolicitante,
                cnpj, arquivoPdf, mensagemExcecao);

        // Validação dos anexos obrigatórios
        validarAnexosObrigatorios(contratoSocial, cnpjCartaoRfb, certificadoFgts, certidaoUniao,
                certidaoSefazPe, daeTfusp, comprovanteDaeTfusp, procuracao, migracaoDecretos, outros,
                mensagemExcecao);

        if (mensagemExcecao.length() > 0) {
            resposta.setStatus("NOK");
            resposta.setMensagemExcecao(mensagemExcecao.toString().trim());
            respostaRepository.save(resposta);
            return resposta;
        }

        // Verificar se há solicitação em andamento
        if (verificarSolicitacaoEmAndamento(cnpj, resposta)) {
            respostaRepository.save(resposta);
            return resposta;
        }

        // Criar e persistir a nova solicitação
        SolicitacaoDTO solicitacao = criarSolicitacao(servicoSolicitacao, nomeSolicitante, telefoneSolicitante,
                emailSolicitante, cnpj, migracaoProdepeProind, naturezaProjeto, estabelecimento,
                quantidadeEmpregos, valorInvestimentos, dataConstituicao, arquivoPdf);

        solicitacaoRepository.save(solicitacao);

        // Criar resposta
        resposta.setStatus("OK");
        resposta.setNumeroProtocolo(solicitacao.getNumeroProtocolo());
        resposta.setAnexoResumo("Link para o PDF de Resumo");
        resposta.setSituacaoSolicitacao("Em Andamento");

        respostaRepository.save(resposta);
        return resposta;
    }

    private void validarCamposObrigatorios(String servicoSolicitacao, String nomeSolicitante, String telefoneSolicitante,
                                           String emailSolicitante, String cnpj, MultipartFile arquivoPdf,
                                           StringBuilder mensagemExcecao) {
        if (servicoSolicitacao == null || servicoSolicitacao.isEmpty()) {
            mensagemExcecao.append("Serviço da Solicitação obrigatório não enviado!\n");
        }
        if (nomeSolicitante == null || nomeSolicitante.isEmpty()) {
            mensagemExcecao.append("Nome do Solicitante obrigatório não enviado!\n");
        }
        if (telefoneSolicitante == null || telefoneSolicitante.isEmpty()) {
            mensagemExcecao.append("Telefone do Solicitante obrigatório não enviado!\n");
        }
        if (emailSolicitante == null || emailSolicitante.isEmpty()) {
            mensagemExcecao.append("E-mail do Solicitante obrigatório não enviado!\n");
        }
        if (cnpj == null || cnpj.isEmpty()) {
            mensagemExcecao.append("CNPJ obrigatório não enviado!\n");
        } else {
            if (solicitacaoRepository.findByCnpj(cnpj).isEmpty()) {
                mensagemExcecao.append("CNPJ não cadastrado!\n");
            }
        }
        if (arquivoPdf == null || arquivoPdf.isEmpty()) {
            mensagemExcecao.append("Arquivo PDF obrigatório não enviado!\n");
        }
    }

    private void validarAnexosObrigatorios(MultipartFile contratoSocial, MultipartFile cnpjCartaoRfb,
                                           MultipartFile certificadoFgts, MultipartFile certidaoUniao,
                                           MultipartFile certidaoSefazPe, MultipartFile daeTfusp,
                                           MultipartFile comprovanteDaeTfusp, MultipartFile procuracao,
                                           MultipartFile migracaoDecretos, MultipartFile outros,
                                           StringBuilder mensagemExcecao) {
        if (contratoSocial == null || contratoSocial.isEmpty()) {
            mensagemExcecao.append("Contrato Social obrigatório não enviado!\n");
        }
        if (cnpjCartaoRfb == null || cnpjCartaoRfb.isEmpty()) {
            mensagemExcecao.append("CNPJ - Cartão da RFB obrigatório não enviado!\n");
        }
        if (certificadoFgts == null || certificadoFgts.isEmpty()) {
            mensagemExcecao.append("Certificado Regularidade FGTS - CRF obrigatório não enviado!\n");
        }
        if (certidaoUniao == null || certidaoUniao.isEmpty()) {
            mensagemExcecao.append("Certidão Regularidade União - CCD obrigatório não enviado!\n");
        }
        if (certidaoSefazPe == null || certidaoSefazPe.isEmpty()) {
            mensagemExcecao.append("Certidão de Regularidade SEFAZ-PE obrigatório não enviado!\n");
        }
        if (daeTfusp == null || daeTfusp.isEmpty()) {
            mensagemExcecao.append("DAE TFUSP obrigatório não enviado!\n");
        }
        if (comprovanteDaeTfusp == null || comprovanteDaeTfusp.isEmpty()) {
            mensagemExcecao.append("Comprovante Pagamento DAE TFUSP obrigatório não enviado!\n");
        }
        if (procuracao == null || procuracao.isEmpty()) {
            mensagemExcecao.append("Procuração obrigatória não enviada!\n");
        }
        // Anexos opcionais
        if (migracaoDecretos == null || migracaoDecretos.isEmpty()) {
            mensagemExcecao.append("Migração de Programa: Decretos Publicados opcional não enviado!\n");
        }
        if (outros == null || outros.isEmpty()) {
            mensagemExcecao.append("Outros anexos opcionais não enviados!\n");
        }
    }

    private boolean verificarSolicitacaoEmAndamento(String cnpj, RespostaDTO resposta) {
        List<SolicitacaoDTO> solicitacoesEmAndamento = solicitacaoRepository.findByCnpj(cnpj);
        Optional<SolicitacaoDTO> solicitacaoEmAndamento = solicitacoesEmAndamento.stream()
                .filter(s -> "Em Andamento".equals(s.getSituacaoSolicitacao()))
                .findFirst();

        if (solicitacaoEmAndamento.isPresent()) {
            resposta.setStatus("NOK");
            resposta.setMensagemExcecao("Já existe uma solicitação em andamento para este CNPJ.\n");
            resposta.setNumeroProtocolo(solicitacaoEmAndamento.get().getNumeroProtocolo());
            return true;
        }

        return false;
    }

    private SolicitacaoDTO criarSolicitacao(String servicoSolicitacao, String nomeSolicitante, String telefoneSolicitante,
                                             String emailSolicitante, String cnpj, String migracaoProdepeProind,
                                             String naturezaProjeto, String estabelecimento, Integer quantidadeEmpregos,
                                             Double valorInvestimentos, LocalDate dataConstituicao, MultipartFile arquivoPdf) {
        SolicitacaoDTO solicitacao = new SolicitacaoDTO();
        solicitacao.setServicoSolicitacao(servicoSolicitacao);
        solicitacao.setNomeSolicitante(nomeSolicitante);
        solicitacao.setTelefoneSolicitante(telefoneSolicitante); // Novo campo
        solicitacao.setEmailSolicitante(emailSolicitante);
        solicitacao.setCnpj(cnpj);
        solicitacao.setMigracaoProdepProind(migracaoProdepeProind);
        solicitacao.setNaturezaProjeto(naturezaProjeto);
        solicitacao.setEstabelecimento(estabelecimento);
        solicitacao.setQuantidadeEmpregos(quantidadeEmpregos);
        solicitacao.setValorInvestimentos(valorInvestimentos);
        solicitacao.setDataConstituicao(dataConstituicao); // Agora é LocalDate diretamente
        solicitacao.setArquivoPdf(arquivoPdf != null ? arquivoPdf.getOriginalFilename() : "Nome não disponível");
        solicitacao.setSituacaoSolicitacao("Em Andamento");

        // Geração de número de protocolo
        String numeroProtocolo = "PROTOCOL-" + System.currentTimeMillis();
        solicitacao.setNumeroProtocolo(numeroProtocolo);

        return solicitacao;
    }
}
