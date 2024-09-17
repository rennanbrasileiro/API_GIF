package com.example.solicitacaoapi.service;

import com.example.solicitacaoapi.model.SolicitacaoDTO;
import com.example.solicitacaoapi.model.RespostaDTO;
import com.example.solicitacaoapi.repository.SolicitacaoRepository;
import com.example.solicitacaoapi.repository.RespostaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public RespostaDTO processarSolicitacao(String servicoSolicitacao, String nomeSolicitante, String emailSolicitante,
                                            String cnpj, String migracaoProdepeProind, String naturezaProjeto, String estabelecimento,
                                            Integer quantidadeEmpregos, Double valorInvestimentos, MultipartFile arquivoPdf,
                                            MultipartFile contratoSocial, MultipartFile cnpjCartaoRfb, MultipartFile certificadoFgts,
                                            MultipartFile certidaoUniao, MultipartFile certidaoSefazPe, MultipartFile daeTfusp,
                                            MultipartFile comprovanteDaeTfusp, MultipartFile procuracao, MultipartFile migracaoDecretos,
                                            MultipartFile outros) {

        RespostaDTO resposta = new RespostaDTO();
        StringBuilder mensagemExcecao = new StringBuilder();

        // Validar campos obrigatórios
        if (servicoSolicitacao == null || servicoSolicitacao.isEmpty()) {
            mensagemExcecao.append("Serviço da Solicitação obrigatório não enviado! ");
        }
        if (nomeSolicitante == null || nomeSolicitante.isEmpty()) {
            mensagemExcecao.append("Nome do Solicitante obrigatório não enviado! ");
        }
        if (emailSolicitante == null || emailSolicitante.isEmpty()) {
            mensagemExcecao.append("E-mail do Solicitante obrigatório não enviado! ");
        }
        if (cnpj == null || cnpj.isEmpty()) {
            mensagemExcecao.append("CNPJ obrigatório não enviado! ");
        } else {
            // Verifica se o CNPJ está cadastrado na base de dados
            List<SolicitacaoDTO> solicitacoesEncontradas = solicitacaoRepository.findByCnpj(cnpj);
            if (solicitacoesEncontradas.isEmpty()) {
                mensagemExcecao.append("CNPJ não cadastrado! ");
            }
        }
        if (arquivoPdf == null || arquivoPdf.isEmpty()) {
            mensagemExcecao.append("Arquivo PDF obrigatório não enviado! ");
        }

        // Validar anexos obrigatórios
        if (contratoSocial == null || contratoSocial.isEmpty()) {
            mensagemExcecao.append("Contrato Social obrigatório não enviado! ");
        }
        if (cnpjCartaoRfb == null || cnpjCartaoRfb.isEmpty()) {
            mensagemExcecao.append("CNPJ - Cartão da RFB obrigatório não enviado! ");
        }
        if (certificadoFgts == null || certificadoFgts.isEmpty()) {
            mensagemExcecao.append("Certificado Regularidade FGTS - CRF obrigatório não enviado! ");
        }
        if (certidaoUniao == null || certidaoUniao.isEmpty()) {
            mensagemExcecao.append("Certidão Regularidade União - CCD obrigatório não enviado! ");
        }
        if (certidaoSefazPe == null || certidaoSefazPe.isEmpty()) {
            mensagemExcecao.append("Certidão de Regularidade SEFAZ-PE obrigatório não enviado! ");
        }
        if (daeTfusp == null || daeTfusp.isEmpty()) {
            mensagemExcecao.append("DAE TFUSP obrigatório não enviado! ");
        }
        if (comprovanteDaeTfusp == null || comprovanteDaeTfusp.isEmpty()) {
            mensagemExcecao.append("Comprovante Pagamento DAE TFUSP obrigatório não enviado! ");
        }
        if (procuracao == null || procuracao.isEmpty()) {
            mensagemExcecao.append("Procuração obrigatória não enviada! ");
        }
        if (migracaoDecretos == null || migracaoDecretos.isEmpty()) {
            mensagemExcecao.append("Migração de Programa: Decretos Publicados opcional não enviado! ");
        }
        if (outros == null || outros.isEmpty()) {
            mensagemExcecao.append("Outros anexos opcional não enviados! ");
        }

        if (mensagemExcecao.length() > 0) {
            resposta.setStatus("NOK");
            resposta.setMensagemExcecao(mensagemExcecao.toString());
            respostaRepository.save(resposta);
            return resposta;
        }

        // Verificar se há alguma solicitação existente em andamento para o mesmo CNPJ
        List<SolicitacaoDTO> solicitacoesEmAndamento = solicitacaoRepository.findByCnpj(cnpj);
        Optional<SolicitacaoDTO> solicitacaoEmAndamento = solicitacoesEmAndamento.stream()
                .filter(s -> "Em Andamento".equals(s.getSituacaoSolicitacao()))
                .findFirst();

        if (solicitacaoEmAndamento.isPresent()) {
            resposta.setStatus("NOK");
            resposta.setMensagemExcecao("Já existe uma solicitação em andamento para este CNPJ.");
            resposta.setNumeroProtocolo(solicitacaoEmAndamento.get().getNumeroProtocolo()); // Retorna o número de protocolo da solicitação em andamento
            respostaRepository.save(resposta);
            return resposta;
        }

        // Criar e persistir a nova solicitação
        SolicitacaoDTO solicitacao = new SolicitacaoDTO();
        solicitacao.setServicoSolicitacao(servicoSolicitacao);
        solicitacao.setNomeSolicitante(nomeSolicitante);
        solicitacao.setEmailSolicitante(emailSolicitante);
        solicitacao.setCnpj(cnpj);
        solicitacao.setMigracaoProdepProind(migracaoProdepeProind);
        solicitacao.setNaturezaProjeto(naturezaProjeto);
        solicitacao.setEstabelecimento(estabelecimento);
        solicitacao.setQuantidadeEmpregos(quantidadeEmpregos);
        solicitacao.setValorInvestimentos(valorInvestimentos);
        solicitacao.setArquivoPdf(arquivoPdf != null ? arquivoPdf.getOriginalFilename() : "Nome não disponível");
        solicitacao.setSituacaoSolicitacao("Em Andamento");

        // Gerar um número de protocolo
        String numeroProtocolo = "PROTOCOL-" + System.currentTimeMillis(); // Exemplo de geração de número de protocolo
        solicitacao.setNumeroProtocolo(numeroProtocolo);

        solicitacaoRepository.save(solicitacao);

        // Criar e retornar resposta
        resposta.setStatus("OK");
        resposta.setNumeroProtocolo(numeroProtocolo);
        resposta.setAnexoResumo("Link para o PDF de Resumo");
        resposta.setSituacaoSolicitacao("Em Andamento");
        respostaRepository.save(resposta);
        return resposta;
    }
}
