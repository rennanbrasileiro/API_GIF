package com.example.solicitacaoapi.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "resposta")
public class RespostaDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
    private String numeroProtocolo;
    private String anexoResumo;
    private String situacaoSolicitacao;
    private String mensagemExcecao;

    // Construtor padrão
    public RespostaDTO() {
    }

    // Construtor para usar em respostas de erro
    public RespostaDTO(String status, String mensagemExcecao) {
        this.status = status;
        this.mensagemExcecao = mensagemExcecao;
    }

    // Construtor para uso geral
    public RespostaDTO(String status, String numeroProtocolo, String anexoResumo, String situacaoSolicitacao) {
        this.status = status;
        this.numeroProtocolo = numeroProtocolo;
        this.anexoResumo = anexoResumo;
        this.situacaoSolicitacao = situacaoSolicitacao;
    }
}
