package com.sptech.school.app_firemanager_notification_service.application.usecases;

import com.sptech.school.app_firemanager_notification_service.application.interfaces.entrada.ExecutarNotificacaoAluno;
import com.sptech.school.app_firemanager_notification_service.application.interfaces.saida.EmailSenderInterface;
import com.sptech.school.app_firemanager_notification_service.application.models.Email;
import com.sptech.school.app_firemanager_notification_service.application.models.PayloadAluno;
import com.sptech.school.app_firemanager_notification_service.domain.models.ConteudoNotificacao;
import com.sptech.school.app_firemanager_notification_service.domain.models.DadosNotificacaoAluno;
import com.sptech.school.app_firemanager_notification_service.domain.service.ConteudoNotificacaoBuilder;

public class EnviarNotificacaoAlunoUseCase implements ExecutarNotificacaoAluno {

    private final ConteudoNotificacaoBuilder conteudoNotificacaoBuilder;
    private final EmailSenderInterface emailSender;

    public EnviarNotificacaoAlunoUseCase(ConteudoNotificacaoBuilder conteudoNotificacaoBuilder, EmailSenderInterface emailSender) {
        this.conteudoNotificacaoBuilder = conteudoNotificacaoBuilder;
        this.emailSender = emailSender;
    }

    @Override
    public void executar(PayloadAluno payload) {

        DadosNotificacaoAluno dados = new DadosNotificacaoAluno(
                payload.agendamentoId(),
                payload.nomeProfessor(),
                payload.data(),
                payload.hora()
        );

        ConteudoNotificacao conteudo = conteudoNotificacaoBuilder.buildParaAluno(payload.status(), dados);

        Email email = new Email(
                payload.emailDestinatario(),
                conteudo.assunto(),
                conteudo.mensagem()
        );

        emailSender.enviar(email);
    }
}
