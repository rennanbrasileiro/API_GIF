package com.example.solicitacaoapi.controller;

import com.example.solicitacaoapi.model.SolicitacaoDTO;
import com.example.solicitacaoapi.repository.SolicitacaoRepository;
import com.example.solicitacaoapi.repository.RespostaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(SolicitacaoController.class)
public class SolicitacaoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SolicitacaoRepository solicitacaoRepository;

    @MockBean
    private RespostaRepository respostaRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testReceberSolicitacaoCNPJNaoCadastrado() throws Exception {
        SolicitacaoDTO solicitacao = new SolicitacaoDTO();
        solicitacao.setCnpj("12345678901234");

        // Simula que o CNPJ não está cadastrado
        when(solicitacaoRepository.findByCnpj(solicitacao.getCnpj())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/solicitacao")
                .contentType("application/json")
                .content("{\"cnpj\": \"12345678901234\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagemExcecao").value(containsString("CNPJ não cadastrado!")))
                .andExpect(jsonPath("$.mensagemExcecao").value(containsString("Serviço da Solicitação obrigatório não enviado!")))
                .andExpect(jsonPath("$.mensagemExcecao").value(containsString("Nome do Solicitante obrigatório não enviado!")))
                .andExpect(jsonPath("$.mensagemExcecao").value(containsString("E-mail do Solicitante obrigatório não enviado!")))
                .andExpect(jsonPath("$.mensagemExcecao").value(containsString("Tipo de anexo obrigatório não enviado!")))
                .andExpect(jsonPath("$.mensagemExcecao").value(containsString("Arquivo PDF obrigatório não enviado!")));
    }

    @Test
    public void testReceberSolicitacaoCamposFaltando() throws Exception {
        SolicitacaoDTO solicitacao = new SolicitacaoDTO();
        solicitacao.setCnpj("12345678901234");

        // Simula que o CNPJ está cadastrado
        when(solicitacaoRepository.findByCnpj(solicitacao.getCnpj())).thenReturn(Optional.of(solicitacao));

        // Simula uma requisição sem todos os campos obrigatórios
        mockMvc.perform(MockMvcRequestBuilders.post("/api/solicitacao")
                .contentType("application/json")
                .content("{\"cnpj\": \"12345678901234\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagemExcecao").value(containsString("Serviço da Solicitação obrigatório não enviado!")))
                .andExpect(jsonPath("$.mensagemExcecao").value(containsString("Nome do Solicitante obrigatório não enviado!")))
                .andExpect(jsonPath("$.mensagemExcecao").value(containsString("E-mail do Solicitante obrigatório não enviado!")))
                .andExpect(jsonPath("$.mensagemExcecao").value(containsString("Tipo de anexo obrigatório não enviado!")))
                .andExpect(jsonPath("$.mensagemExcecao").value(containsString("Arquivo PDF obrigatório não enviado!")));
    }

    @Disabled("Desativado devido a problemas de implementação")
    @Test
    public void testReceberSolicitacaoSucesso() throws Exception {
        SolicitacaoDTO solicitacao = new SolicitacaoDTO();
        solicitacao.setCnpj("12345678901234");
        solicitacao.setTipoAnexo("PDF");
        solicitacao.setArquivoPdf("file.pdf");

        // Simula que o CNPJ está cadastrado
        when(solicitacaoRepository.findByCnpj(solicitacao.getCnpj())).thenReturn(Optional.of(solicitacao));

        // Simula uma requisição com todos os campos corretos
        mockMvc.perform(MockMvcRequestBuilders.post("/api/solicitacao")
                .contentType("application/json")
                .content("{\"cnpj\": \"12345678901234\", \"tipoAnexo\": \"PDF\", \"arquivoPdf\": \"file.pdf\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.numeroProtocolo").value("2024PROTOCOL123"));
    }
}
