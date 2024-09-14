-- Criação da tabela solicitacao
CREATE TABLE solicitacao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    servicoSolicitacao VARCHAR(255),
    nomeSolicitante VARCHAR(255),
    emailSolicitante VARCHAR(255),
    cnpj VARCHAR(18),
    migracaoProdepProind VARCHAR(10),
    naturezaProjeto VARCHAR(255),
    estabelecimento VARCHAR(255),
    qtdEmpregos INT,
    valorInvestimento DOUBLE,
    tipoAnexo VARCHAR(10),
    arquivoPdf VARCHAR(255)
);

-- Criação da tabela resposta
CREATE TABLE resposta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    status VARCHAR(10),
    numeroProtocolo VARCHAR(50),
    anexoResumo VARCHAR(255),
    situacaoSolicitacao VARCHAR(20),
    mensagemExcecao VARCHAR(255)
);

