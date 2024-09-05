package com.example.solicitacaoapi.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "solicitacao")
public class SolicitacaoDTO {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String servicoSolicitacao;
    private String nomeSolicitante;
    private String emailSolicitante;
    private String cnpj;
    private String migracaoProdepProind;
    private String naturezaProjeto;
    private String estabelecimento;
    private Integer qtdEmpregos;
    private Double valorInvestimento;
    private String tipoAnexo;
    private String arquivoPdf;
}
