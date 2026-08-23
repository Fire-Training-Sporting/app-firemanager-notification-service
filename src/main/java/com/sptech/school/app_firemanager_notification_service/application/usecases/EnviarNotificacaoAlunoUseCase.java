package com.sptech.school.app_firemanager_notification_service.application.usecases;

import com.sptech.school.app_firemanager_notification_service.application.interfaces.entrada.ExecutarNotificacaoAluno;
import com.sptech.school.app_firemanager_notification_service.application.interfaces.saida.EmailSenderInterface;
import com.sptech.school.app_firemanager_notification_service.application.interfaces.saida.RenderizarEmailAluno;
import com.sptech.school.app_firemanager_notification_service.application.models.Email;
import com.sptech.school.app_firemanager_notification_service.application.models.payload.PayloadAluno;
import com.sptech.school.app_firemanager_notification_service.domain.models.ConteudoNotificacao;
import com.sptech.school.app_firemanager_notification_service.domain.models.DadosNotificacaoAluno;
import com.sptech.school.app_firemanager_notification_service.domain.service.ConteudoNotificacaoBuilder;
import org.springframework.stereotype.Service;

@Service
public class EnviarNotificacaoAlunoUseCase implements ExecutarNotificacaoAluno {

    private final ConteudoNotificacaoBuilder conteudoNotificacaoBuilder;
    private final EmailSenderInterface emailSender;
    private final RenderizarEmailAluno renderizador;

    public EnviarNotificacaoAlunoUseCase(ConteudoNotificacaoBuilder conteudoNotificacaoBuilder, EmailSenderInterface emailSender, RenderizarEmailAluno renderizador) {
        this.conteudoNotificacaoBuilder = conteudoNotificacaoBuilder;
        this.emailSender = emailSender;
        this.renderizador = renderizador;
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

        String emailHtml = renderizador.renderizar(conteudo, dados);

        Email email = new Email(
                payload.emailDestinatario(),
                conteudo.assunto(),
                emailHtml
        );

        emailSender.enviar(email);
    }
}
