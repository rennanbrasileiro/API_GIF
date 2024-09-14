-- Inserindo dados de teste na tabela solicitacao
INSERT INTO solicitacao (servico_solicitacao, nome_solicitante, email_solicitante, cnpj, migracao_prodep_proind, natureza_projeto, estabelecimento, qtd_empregos, valor_investimento, tipo_anexo, arquivo_pdf) VALUES
('Serviço de Tecnologia', 'Ana Souza', 'ana.souza@example.com', '11.111.111/0001-11', 'S', 'Desenvolvimento de Software', 'Tech Solutions', 25, 500000.00, 'PDF', 'tecnologia.pdf'),
('Consultoria Empresarial', 'Carlos Lima', 'carlos.lima@example.com', '22.222.222/0001-22', 'N', 'Consultoria para Negócios', 'Consultoria & Cia', 10, 150000.00, 'DOC', 'consultoria.docx'),
('Projeto Educacional', 'Fernanda Costa', 'fernanda.costa@example.com', '33.333.333/0001-33', 'S', 'Desenvolvimento de Curso', 'EducaPro', 5, 75000.00, 'PDF', 'educacional.pdf'),
('Inovação em Saúde', 'Gabriel Oliveira', 'gabriel.oliveira@example.com', '44.444.444/0001-44', 'S', 'Pesquisa e Desenvolvimento', 'HealthInnovations', 12, 200000.00, 'XLSX', 'saude.xlsx'),
('Projeto Sustentabilidade', 'Juliana Martins', 'juliana.martins@example.com', '55.555.555/0001-55', 'N', 'Sustentabilidade e Meio Ambiente', 'EcoFuture', 8, 120000.00, 'PDF', 'sustentabilidade.pdf'),
('Expansão de Mercado', 'Leonardo Pereira', 'leonardo.pereira@example.com', '66.666.666/0001-66', 'S', 'Expansão Nacional', 'MarketExpand', 15, 300000.00, 'PDF', 'mercado.pdf'),
('Desenvolvimento de Hardware', 'Mariana Silva', 'mariana.silva@example.com', '77.777.777/0001-77', 'N', 'Projeto de Hardware', 'HardwareDev', 20, 400000.00, 'DOC', 'hardware.docx'),
('Serviço de Consultoria Jurídica', 'Rodrigo Almeida', 'rodrigo.almeida@example.com', '88.888.888/0001-88', 'S', 'Consultoria Jurídica Especializada', 'LegalExperts', 7, 100000.00, 'PDF', 'juridica.pdf');

-- Inserindo dados de teste na tabela resposta
-- Inserir dados na tabela 'resposta'
INSERT INTO resposta (status, numero_protocolo, anexo_resumo, situacao_solicitacao, mensagem_excecao) VALUES
('OK', 'PROTOCOLO-001', 'resumo-tecnologia.pdf', 'Em andamento', NULL),
('NOK', 'PROTOCOLO-002', 'resumo-consultoria.docx', NULL, 'CNPJ não cadastrado!'),
('OK', 'PROTOCOLO-003', 'resumo-educacional.pdf', 'Em andamento', NULL),
('OK', 'PROTOCOLO-004', 'resumo-saude.xlsx', 'Em andamento', NULL),
('NOK', 'PROTOCOLO-005', 'resumo-sustentabilidade.pdf', NULL, 'Documentação incompleta!'),
('OK', 'PROTOCOLO-006', 'resumo-mercado.pdf', 'Em andamento', NULL),
('NOK', 'PROTOCOLO-007', 'resumo-hardware.docx', NULL, 'Erro de processamento!'),
('OK', 'PROTOCOLO-008', 'resumo-juridica.pdf', 'Em andamento', NULL);
