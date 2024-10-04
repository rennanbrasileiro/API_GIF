CREATE TABLE solicitacao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    servico_solicitacao VARCHAR(255),
    nome_solicitante VARCHAR(255),
    telefone_solicitante VARCHAR(20),
    email_solicitante VARCHAR(255),
    cnpj VARCHAR(20),
    migracao_prodep_proind CHAR(1),  
    natureza_projeto VARCHAR(255),
    estabelecimento VARCHAR(255),
    quantidade_empregos INTEGER,
    valor_investimentos DECIMAL(10, 2),
    data_constituicao DATE,
    tipo_anexo VARCHAR(50),
    arquivo_pdf VARCHAR(255)
);

-- Criação da tabela 'resposta'
CREATE TABLE resposta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    status VARCHAR(255),
    numero_protocolo VARCHAR(255),  
    anexo_resumo VARCHAR(255),
    situacao_solicitacao VARCHAR(255),
    mensagem_excecao TEXT
);
