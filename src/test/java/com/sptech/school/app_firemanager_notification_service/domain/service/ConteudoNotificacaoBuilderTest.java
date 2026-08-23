package com.sptech.school.app_firemanager_notification_service.domain.service;

import com.sptech.school.app_firemanager_notification_service.domain.enums.StatusAgendamento;
import com.sptech.school.app_firemanager_notification_service.domain.models.ConteudoNotificacao;
import com.sptech.school.app_firemanager_notification_service.domain.models.DadosNotificacaoAluno;
import com.sptech.school.app_firemanager_notification_service.domain.models.DadosNotificacaoProfessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConteudoNotificacaoBuilderTest {

    private ConteudoNotificacaoBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new ConteudoNotificacaoBuilder();
    }

    // ============================================================
    // ALUNO
    // ============================================================

    @Test
    void deveConstruirNotificacaoParaAlunoQuandoStatusForPendente() {

        DadosNotificacaoAluno dados = new DadosNotificacaoAluno(
                123L,
                "João Silva",
                LocalDate.of(2026, 8, 25),
                LocalTime.of(14, 30)
        );

        ConteudoNotificacao resultado =
                builder.buildParaAluno(StatusAgendamento.PENDENTE, dados);

        assertEquals(
                "[FireManager] - Nova Aula Agendada!",
                resultado.assunto()
        );

        assertEquals(
                "Você tem uma nova aula agendada com João Silva no dia 25/08/2026 às 14:30. [ID Agendamento: 123]",
                resultado.mensagem()
        );
    }

    @Test
    void deveConstruirNotificacaoParaAlunoQuandoStatusForCancelado() {

        DadosNotificacaoAluno dados = new DadosNotificacaoAluno(
                123L,
                "João Silva",
                LocalDate.of(2026, 8, 25),
                LocalTime.of(14, 30)
        );

        ConteudoNotificacao resultado =
                builder.buildParaAluno(StatusAgendamento.CANCELADO, dados);

        assertEquals(
                "[FireManager] - Agendamento Cancelado",
                resultado.assunto()
        );

        assertEquals(
                "Seu agendamento foi cancelado. [ID Agendamento: 123]",
                resultado.mensagem()
        );
    }

    @Test
    void deveConstruirNotificacaoParaAlunoQuandoStatusForConfirmado() {

        DadosNotificacaoAluno dados = new DadosNotificacaoAluno(
                123L,
                "João Silva",
                LocalDate.of(2026, 8, 25),
                LocalTime.of(14, 30)
        );

        ConteudoNotificacao resultado =
                builder.buildParaAluno(StatusAgendamento.CONFIRMADO, dados);

        assertEquals(
                "[FireManager] - Aula Confirmada",
                resultado.assunto()
        );

        assertEquals(
                "Sua aula com João Silva é daqui menos de 24h! [ID Agendamento: 123]",
                resultado.mensagem()
        );
    }

    @Test
    void deveConstruirNotificacaoParaAlunoQuandoStatusForFinalizado() {

        DadosNotificacaoAluno dados = new DadosNotificacaoAluno(
                123L,
                "João Silva",
                LocalDate.of(2026, 8, 25),
                LocalTime.of(14, 30)
        );

        ConteudoNotificacao resultado =
                builder.buildParaAluno(StatusAgendamento.FINALIZADO, dados);

        assertEquals(
                "[FireManager] - Aula Concluída",
                resultado.assunto()
        );

        assertEquals(
                "Sua aula com João Silva foi concluída. Obrigado pela confiança em nosso serviço! [ID Agendamento: 123]",
                resultado.mensagem()
        );
    }

    // ============================================================
    // PROFESSOR
    // ============================================================

    @Test
    void deveConstruirNotificacaoParaProfessorQuandoStatusForPendente() {

        DadosNotificacaoProfessor dados = new DadosNotificacaoProfessor(
                123L,
                "João Silva",
                "11999999999",
                "Condomínio Alpha",
                "Levar material",
                LocalDate.of(2026, 8, 25),
                LocalTime.of(14, 30)
        );

        ConteudoNotificacao resultado =
                builder.buildParaProfessor(StatusAgendamento.PENDENTE, dados);

        assertEquals(
                "[FireManager] - Nova Aula Agendada!",
                resultado.assunto()
        );

        assertEquals(
                "Você tem uma nova aula agendada com João Silva no dia 25/08/2026 às 14:30. [ID Agendamento: 123]",
                resultado.mensagem()
        );
    }

    @Test
    void deveConstruirNotificacaoParaProfessorQuandoStatusForPendenteObservacaoVazia() {

        DadosNotificacaoProfessor dados = new DadosNotificacaoProfessor(
                123L,
                "João Silva",
                "11999999999",
                "Condomínio Alpha",
                null,
                LocalDate.of(2026, 8, 25),
                LocalTime.of(14, 30)
        );

        ConteudoNotificacao resultado =
                builder.buildParaProfessor(StatusAgendamento.PENDENTE, dados);

        assertEquals(
                "[FireManager] - Nova Aula Agendada!",
                resultado.assunto()
        );

        assertEquals(
                "Você tem uma nova aula agendada com João Silva no dia 25/08/2026 às 14:30. [ID Agendamento: 123]",
                resultado.mensagem()
        );
    }

    @Test
    void deveConstruirNotificacaoParaProfessorQuandoStatusForCancelado() {

        DadosNotificacaoProfessor dados = new DadosNotificacaoProfessor(
                123L,
                "João Silva",
                "11999999999",
                "Condomínio Alpha",
                "Levar material",
                LocalDate.of(2026, 8, 25),
                LocalTime.of(14, 30)
        );

        ConteudoNotificacao resultado =
                builder.buildParaProfessor(StatusAgendamento.CANCELADO, dados);

        assertEquals(
                "[FireManager] - Agendamento Cancelado",
                resultado.assunto()
        );

        assertEquals(
                "O agendamento com João Silva no dia 25/08/2026 às 14:30 foi cancelado. [ID Agendamento: 123]",
                resultado.mensagem()
        );
    }

    @Test
    void deveConstruirNotificacaoParaProfessorQuandoStatusForConfirmado() {

        DadosNotificacaoProfessor dados = new DadosNotificacaoProfessor(
                123L,
                "João Silva",
                "11999999999",
                "Condomínio Alpha",
                "Levar material",
                LocalDate.of(2026, 8, 25),
                LocalTime.of(14, 30)
        );

        ConteudoNotificacao resultado =
                builder.buildParaProfessor(StatusAgendamento.CONFIRMADO, dados);

        assertEquals(
                "[FireManager] - Aula Confirmada",
                resultado.assunto()
        );

        assertEquals(
                "A aula com João Silva é daqui menos de 24h e não pode mais ser cancelada. [ID Agendamento: 123]",
                resultado.mensagem()
        );
    }

    @Test
    void deveConstruirNotificacaoParaProfessorQuandoStatusForFinalizado() {

        DadosNotificacaoProfessor dados = new DadosNotificacaoProfessor(
                123L,
                "João Silva",
                "11999999999",
                "Condomínio Alpha",
                "Levar material",
                LocalDate.of(2026, 8, 25),
                LocalTime.of(14, 30)
        );

        ConteudoNotificacao resultado =
                builder.buildParaProfessor(StatusAgendamento.FINALIZADO, dados);

        assertEquals(
                "[FireManager] - Aula Concluída",
                resultado.assunto()
        );

        assertEquals(
                "A aula com João Silva realizada em 25/08/2026 às 14:30 foi concluída. [ID Agendamento: 123]",
                resultado.mensagem()
        );
    }
}