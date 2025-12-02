SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'cinemadb'
  AND pid <> pg_backend_pid();

DROP DATABASE IF EXISTS cinemadb;

CREATE DATABASE cinemadb;

CREATE TABLE Filmes (
    filme_id SERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    duracao_minutos INT NOT NULL,
    genero VARCHAR(100),
    classificacao_etaria VARCHAR(10)
);

CREATE TABLE Salas (
    sala_id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    capacidade INT NOT NULL CHECK (capacidade > 0)
);

CREATE TABLE Sessoes (
    sessao_id SERIAL PRIMARY KEY,
    filme_id INT REFERENCES Filmes(filme_id) ON DELETE CASCADE,
    sala_id INT REFERENCES Salas(sala_id) ON DELETE RESTRICT,
    data_hora TIMESTAMP NOT NULL,
    preco NUMERIC(5, 2) NOT NULL CHECK (preco >= 0),
    CONSTRAINT uq_sessao_sala_data UNIQUE (sala_id, data_hora) -- Evita sessões concorrentes
);

CREATE TABLE Ingressos (
    ingresso_id SERIAL PRIMARY KEY,
    sessao_id INT REFERENCES Sessoes(sessao_id) ON DELETE CASCADE,
    num_assento INT NOT NULL,
    vendido_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_sessao_assento UNIQUE (sessao_id, num_assento)
);

CREATE TABLE Relatorio_Lotacao (
    sessao_id INT PRIMARY KEY REFERENCES Sessoes(sessao_id),
    total_vendidos INT NOT NULL DEFAULT 0,
    capacidade_sala INT NOT NULL,
    percentual_ocupacao NUMERIC(5, 2) NOT NULL DEFAULT 0.00
);



CREATE OR REPLACE FUNCTION contar_ingressos_vendidos(p_sessao_id INT)
RETURNS INT AS $$
BEGIN
    RETURN (SELECT COUNT(*) FROM Ingressos WHERE sessao_id = p_sessao_id);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE vender_ingresso(
    p_sessao_id INT,
    p_num_assento INT,
    OUT p_sucesso BOOLEAN,
    OUT p_mensagem VARCHAR
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_capacidade INT;
    v_vendidos INT;
BEGIN
    SELECT s.capacidade, contar_ingressos_vendidos(p_sessao_id)
    INTO v_capacidade, v_vendidos
    FROM Sessoes ses
    JOIN Salas s ON ses.sala_id = s.sala_id
    WHERE ses.sessao_id = p_sessao_id;

    IF v_capacidade IS NULL THEN
        p_sucesso := FALSE;
        p_mensagem := 'Erro: Sessão não encontrada.';
        RETURN;
    END IF;

    IF v_vendidos >= v_capacidade THEN
        p_sucesso := FALSE;
        p_mensagem := 'Erro: A sessão está lotada. Capacidade máxima atingida (' || v_capacidade || ').';
        RETURN;
    END IF;

    IF p_num_assento <= 0 OR p_num_assento > v_capacidade THEN
        p_sucesso := FALSE;
        p_mensagem := 'Erro: Número do assento (' || p_num_assento || ') está fora do intervalo válido (1-' || v_capacidade || ').';
        RETURN;
    END IF;
    
    INSERT INTO Ingressos (sessao_id, num_assento)
    VALUES (p_sessao_id, p_num_assento);

    p_sucesso := TRUE;
    p_mensagem := 'Ingresso vendido com sucesso para o assento ' || p_num_assento || '.';

EXCEPTION
    WHEN unique_violation THEN
        p_sucesso := FALSE;
        p_mensagem := 'Erro: O assento ' || p_num_assento || ' já está vendido para esta sessão.';
    WHEN OTHERS THEN
        p_sucesso := FALSE;
        p_mensagem := 'Erro desconhecido ao vender ingresso: ' || SQLERRM;
        ROLLBACK;
END;
$$;


CREATE OR REPLACE FUNCTION atualizar_relatorio_lotacao()
RETURNS TRIGGER AS $$
DECLARE
    v_capacidade INT;
    v_total INT;
BEGIN
    v_total := contar_ingressos_vendidos(NEW.sessao_id);

    SELECT s.capacidade INTO v_capacidade
    FROM Sessoes ses
    JOIN Salas s ON ses.sala_id = s.sala_id
    WHERE ses.sessao_id = NEW.sessao_id;
    
    INSERT INTO Relatorio_Lotacao (sessao_id, total_vendidos, capacidade_sala, percentual_ocupacao)
    VALUES (NEW.sessao_id, v_total, v_capacidade, (v_total::NUMERIC * 100) / v_capacidade)
    ON CONFLICT (sessao_id) DO UPDATE
    SET total_vendidos = EXCLUDED.total_vendidos,
        percentual_ocupacao = EXCLUDED.percentual_ocupacao;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tg_atualizar_relatorio_lotacao
AFTER INSERT ON Ingressos
FOR EACH ROW
EXECUTE FUNCTION atualizar_relatorio_lotacao();

INSERT INTO Filmes (titulo, duracao_minutos, genero, classificacao_etaria) 
VALUES ('O Retorno do Java King', 150, 'Aventura', 'Livre');

INSERT INTO Salas (nome, capacidade) 
VALUES ('Sala Principal (CineTech)', 100);

INSERT INTO Sessoes (filme_id, sala_id, data_hora, preco) 
VALUES (1, 1, '2025-12-05 19:00:00', 35.00);