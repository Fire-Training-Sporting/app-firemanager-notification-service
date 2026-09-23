package com.sptech.school.app_firemanager_notification_service.infrastructure.rabbitmq.listener;

import com.sptech.school.app_firemanager_notification_service.application.interfaces.entrada.ExecutarNotificacaoAluno;
import com.sptech.school.app_firemanager_notification_service.application.interfaces.entrada.ExecutarNotificacaoProfessor;
import com.sptech.school.app_firemanager_notification_service.application.models.payload.PayloadAluno;
import com.sptech.school.app_firemanager_notification_service.application.models.payload.PayloadProfessor;
import com.sptech.school.app_firemanager_notification_service.domain.enums.StatusAgendamento;
import com.sptech.school.app_firemanager_notification_service.infrastructure.rabbitmq.dto.NotificacaoAlunoMensagem;
import com.sptech.school.app_firemanager_notification_service.infrastructure.rabbitmq.dto.NotificacaoProfessorMensagem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.sptech.school.app_firemanager_notification_service.infrastructure.rabbitmq.configuration.RabbitMQConfig.FILA_NOTIFICACAO_ALUNO;
import static com.sptech.school.app_firemanager_notification_service.infrastructure.rabbitmq.configuration.RabbitMQConfig.FILA_NOTIFICACAO_PROFESSOR;

@Component
public class NotificacaoListener {

    private final ExecutarNotificacaoProfessor executarNotificacaoProfessor;
    private final ExecutarNotificacaoAluno executarNotificacaoAluno;
    private static final Logger log = LoggerFactory.getLogger(NotificacaoListener.class);

    public NotificacaoListener(ExecutarNotificacaoProfessor executarNotificacaoProfessor, ExecutarNotificacaoAluno executarNotificacaoAluno) {
        this.executarNotificacaoProfessor = executarNotificacaoProfessor;
        this.executarNotificacaoAluno = executarNotificacaoAluno;
    }

    @RabbitListener(queues = FILA_NOTIFICACAO_ALUNO)
    public void consumirNotificacaoAluno(NotificacaoAlunoMensagem mensagem) {

        log.info("Mensagem recebida na fila '{}' - agendamentoId={}, status={}",
                FILA_NOTIFICACAO_ALUNO, mensagem.agendamentoId(), mensagem.status());

        PayloadAluno payload = new PayloadAluno(
                mensagem.agendamentoId(),
                mensagem.nomeProfessor(),
                mensagem.data(),
                mensagem.hora(),
                StatusAgendamento.valueOf(mensagem.status().toUpperCase()),
                mensagem.emailDestinatario()
        );

        executarNotificacaoAluno.executar(payload);
    }

    @RabbitListener(queues = FILA_NOTIFICACAO_PROFESSOR)
    public void consumirNotificacaoProfessor(NotificacaoProfessorMensagem mensagem) {

        log.info("Mensagem recebida na fila '{}' - agendamentoId={}, status={}",
                FILA_NOTIFICACAO_PROFESSOR, mensagem.agendamentoId(), mensagem.status());

        PayloadProfessor payload = new PayloadProfessor(
                mensagem.agendamentoId(),
                mensagem.nomeAluno(),
                mensagem.telefoneAluno(),
                mensagem.nomeCondominio(),
                mensagem.observacao(),
                mensagem.data(),
                mensagem.hora(),
                StatusAgendamento.valueOf(mensagem.status().toUpperCase()),
                mensagem.emailDestinatario()
        );

        executarNotificacaoProfessor.executar(payload);
    }
}
