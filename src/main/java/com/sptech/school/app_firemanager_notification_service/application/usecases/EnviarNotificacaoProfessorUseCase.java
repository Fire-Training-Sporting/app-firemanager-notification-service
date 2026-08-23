package com.sptech.school.app_firemanager_notification_service.application.usecases;

import com.sptech.school.app_firemanager_notification_service.application.interfaces.entrada.ExecutarNotificacaoProfessor;
import com.sptech.school.app_firemanager_notification_service.application.interfaces.saida.EmailSenderInterface;
import com.sptech.school.app_firemanager_notification_service.application.interfaces.saida.RenderizarEmailProfessor;
import com.sptech.school.app_firemanager_notification_service.application.models.Email;
import com.sptech.school.app_firemanager_notification_service.application.models.payload.PayloadProfessor;
import com.sptech.school.app_firemanager_notification_service.domain.models.ConteudoNotificacao;
import com.sptech.school.app_firemanager_notification_service.domain.models.DadosNotificacaoProfessor;
import com.sptech.school.app_firemanager_notification_service.domain.service.ConteudoNotificacaoBuilder;
import org.springframework.stereotype.Service;

@Service
public class EnviarNotificacaoProfessorUseCase implements ExecutarNotificacaoProfessor {

    private final ConteudoNotificacaoBuilder conteudoNotificacaoBuilder;
    private final EmailSenderInterface emailSender;
    private final RenderizarEmailProfessor renderizador;

    public EnviarNotificacaoProfessorUseCase(ConteudoNotificacaoBuilder conteudoNotificacaoBuilder, EmailSenderInterface emailSender, RenderizarEmailProfessor renderizador) {
        this.conteudoNotificacaoBuilder = conteudoNotificacaoBuilder;
        this.emailSender = emailSender;
        this.renderizador = renderizador;
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

        String emailHtml = renderizador.renderizar(conteudo, dados);

        Email email = new Email(
                payload.emailDestinatario(),
                conteudo.assunto(),
                emailHtml
        );

        emailSender.enviar(email);
    }
}
