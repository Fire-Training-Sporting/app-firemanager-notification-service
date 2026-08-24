package com.sptech.school.app_firemanager_notification_service.domain.service;

import com.sptech.school.app_firemanager_notification_service.domain.enums.StatusAgendamento;
import com.sptech.school.app_firemanager_notification_service.domain.models.ConteudoNotificacao;
import com.sptech.school.app_firemanager_notification_service.domain.models.DadosNotificacaoAluno;
import com.sptech.school.app_firemanager_notification_service.domain.models.DadosNotificacaoProfessor;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class ConteudoNotificacaoBuilderTest {

    private final ConteudoNotificacaoBuilder builder = new ConteudoNotificacaoBuilder();

    private final DadosNotificacaoAluno dadosAluno = new DadosNotificacaoAluno(
            42L, "Professor João", LocalDate.of(2026, 8, 30), LocalTime.of(14, 0)
    );

    private final DadosNotificacaoProfessor dadosProfessor = new DadosNotificacaoProfessor(
            42L, "Maria Aluna", "11999990000", "Condomínio Sol", "Levar tatame",
            LocalDate.of(2026, 8, 30), LocalTime.of(14, 0)
    );

    // ---- ALUNO ----

    @Test
    void buildParaAluno_pendente() {
        ConteudoNotificacao conteudo = builder.buildParaAluno(StatusAgendamento.PENDENTE, dadosAluno);

        assertThat(conteudo.assunto()).isEqualTo("[FireManager] - Nova Aula Agendada!");
        assertThat(conteudo.mensagem())
                .contains("Professor João")
                .contains("30/08/2026")
                .contains("14:00")
                .contains("42");
    }

    @Test
    void buildParaAluno_cancelado() {
        ConteudoNotificacao conteudo = builder.buildParaAluno(StatusAgendamento.CANCELADO, dadosAluno);

        assertThat(conteudo.assunto()).isEqualTo("[FireManager] - Agendamento Cancelado");
        assertThat(conteudo.mensagem()).contains("cancelado").contains("42");
    }

    @Test
    void buildParaAluno_confirmado() {
        ConteudoNotificacao conteudo = builder.buildParaAluno(StatusAgendamento.CONFIRMADO, dadosAluno);

        assertThat(conteudo.assunto()).isEqualTo("[FireManager] - Aula Confirmada");
        assertThat(conteudo.mensagem()).contains("Professor João").contains("42");
    }

    @Test
    void buildParaAluno_finalizado() {
        ConteudoNotificacao conteudo = builder.buildParaAluno(StatusAgendamento.FINALIZADO, dadosAluno);

        assertThat(conteudo.assunto()).isEqualTo("[FireManager] - Aula Concluída");
        assertThat(conteudo.mensagem()).contains("Professor João").contains("42");
    }

    // ---- PROFESSOR ----

    @Test
    void buildParaProfessor_pendente() {
        ConteudoNotificacao conteudo = builder.buildParaProfessor(StatusAgendamento.PENDENTE, dadosProfessor);

        assertThat(conteudo.assunto()).isEqualTo("[FireManager] - Nova Aula Agendada!");
        assertThat(conteudo.mensagem())
                .contains("Maria Aluna")
                .contains("30/08/2026")
                .contains("14:00")
                .contains("42");
    }

    @Test
    void buildParaProfessor_cancelado() {
        ConteudoNotificacao conteudo = builder.buildParaProfessor(StatusAgendamento.CANCELADO, dadosProfessor);

        assertThat(conteudo.assunto()).isEqualTo("[FireManager] - Agendamento Cancelado");
        assertThat(conteudo.mensagem()).contains("Maria Aluna").contains("42");
    }

    @Test
    void buildParaProfessor_confirmado() {
        ConteudoNotificacao conteudo = builder.buildParaProfessor(StatusAgendamento.CONFIRMADO, dadosProfessor);

        assertThat(conteudo.assunto()).isEqualTo("[FireManager] - Aula Confirmada");
        assertThat(conteudo.mensagem()).contains("Maria Aluna").contains("42");
    }

    @Test
    void buildParaProfessor_finalizado() {
        ConteudoNotificacao conteudo = builder.buildParaProfessor(StatusAgendamento.FINALIZADO, dadosProfessor);

        assertThat(conteudo.assunto()).isEqualTo("[FireManager] - Aula Concluída");
        assertThat(conteudo.mensagem()).contains("Maria Aluna").contains("42");
    }
}