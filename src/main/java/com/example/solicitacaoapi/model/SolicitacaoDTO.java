package com.example.solicitacaoapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "solicitacao")
public class SolicitacaoDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Serviço de solicitação é obrigatório")
    @Column(nullable = false)
    private String servicoSolicitacao;

    @NotBlank(message = "Nome do solicitante é obrigatório")
    @Column(nullable = false)
    private String nomeSolicitante;

    @NotBlank(message = "Telefone do solicitante é obrigatório")
    @Column(nullable = false)
    private String telefoneSolicitante;

    @Email(message = "Email do solicitante deve ser válido")
    @NotBlank(message = "Email do solicitante é obrigatório")
    @Column(nullable = false)
    private String emailSolicitante;

    @NotBlank(message = "CNPJ é obrigatório")
    @Column(nullable = false, unique = true)
    private String cnpj;

    @NotBlank(message = "Migração Prodep Proind é obrigatória")
    @Column(nullable = false)
    private String migracaoProdepProind;

    @NotBlank(message = "Natureza do projeto é obrigatória")
    @Column(nullable = false)
    private String naturezaProjeto;

    @NotBlank(message = "Estabelecimento é obrigatório")
    @Column(nullable = false)
    private String estabelecimento;

    @NotNull(message = "Quantidade de empregos é obrigatória")
    @Column(nullable = false)
    private Integer quantidadeEmpregos;

    @NotNull(message = "Valor de investimento é obrigatório")
    @Column(nullable = false)
    private Double valorInvestimentos;

    @NotNull(message = "Data de constituição é obrigatória") // Alterado para NotNull
    @Column(nullable = false)
    private LocalDate dataConstituicao; // Alterado para LocalDate

    private String arquivoPdf; // Nome do arquivo ou link para o PDF

    private String situacaoSolicitacao;  
    private String numeroProtocolo;
}
