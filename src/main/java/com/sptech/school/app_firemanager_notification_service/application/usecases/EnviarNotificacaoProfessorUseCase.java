package com.sptech.school.app_firemanager_notification_service.application.usecases;

import com.sptech.school.app_firemanager_notification_service.application.interfaces.entrada.ExecutarNotificacaoProfessor;
import com.sptech.school.app_firemanager_notification_service.application.interfaces.saida.EmailSenderInterface;
import com.sptech.school.app_firemanager_notification_service.application.models.Email;
import com.sptech.school.app_firemanager_notification_service.application.models.PayloadProfessor;
import com.sptech.school.app_firemanager_notification_service.domain.models.ConteudoNotificacao;
import com.sptech.school.app_firemanager_notification_service.domain.models.DadosNotificacaoProfessor;
import com.sptech.school.app_firemanager_notification_service.domain.service.ConteudoNotificacaoBuilder;

public class EnviarNotificacaoProfessorUseCase implements ExecutarNotificacaoProfessor {

    private final ConteudoNotificacaoBuilder conteudoNotificacaoBuilder;
    private final EmailSenderInterface emailSender;

    public EnviarNotificacaoProfessorUseCase(ConteudoNotificacaoBuilder conteudoNotificacaoBuilder, EmailSenderInterface emailSender) {
        this.conteudoNotificacaoBuilder = conteudoNotificacaoBuilder;
        this.emailSender = emailSender;
    }

    @Override
    public void executar(PayloadProfessor payload) {

        DadosNotificacaoProfessor dados = new DadosNotificacaoProfessor(
                payload.agendamentoId(),
                payload.nomeAluno(),
                payload.telefoneAluno(),
                payload.nomeCondominio(),
                payload.observacao(),
                payload.data(),
                payload.hora()
        );

        ConteudoNotificacao conteudo = conteudoNotificacaoBuilder.buildParaProfessor(payload.status(), dados);

        Email email = new Email(
                payload.emailDestinatario(),
                conteudo.assunto(),
                conteudo.mensagem()
        );

        emailSender.enviar(email);
    }
}
