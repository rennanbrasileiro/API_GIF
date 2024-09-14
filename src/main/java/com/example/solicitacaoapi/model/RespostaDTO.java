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
}
