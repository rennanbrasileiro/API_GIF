package com.example.solicitacaoapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "solicitacao")
public class SolicitacaoDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Serviço de solicitação é obrigatório")
    private String servicoSolicitacao;

    @NotBlank(message = "Nome do solicitante é obrigatório")
    private String nomeSolicitante;

    @Email(message = "Email do solicitante deve ser válido")
    @NotBlank(message = "Email do solicitante é obrigatório")
    private String emailSolicitante;

    @NotBlank(message = "CNPJ é obrigatório")
    private String cnpj;

    @NotBlank(message = "Migração Prodep Proind é obrigatória")
    private String migracaoProdepProind;

    @NotBlank(message = "Natureza do projeto é obrigatória")
    private String naturezaProjeto;

    @NotBlank(message = "Estabelecimento é obrigatório")
    private String estabelecimento;

    @NotNull(message = "Quantidade de empregos é obrigatória")
    private Integer quantidadeEmpregos;

    @NotNull(message = "Valor de investimento é obrigatório")
    private Double valorInvestimentos;

    private String arquivoPdf; // Assumindo que o PDF é armazenado como um link ou nome de arquivo
}
