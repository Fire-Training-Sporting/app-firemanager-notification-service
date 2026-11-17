package com.sptech.school.app_firemanager_notification_service.application.usecases;

import com.sptech.school.app_firemanager_notification_service.application.interfaces.entrada.ExecutarNotificacaoRecuperacaoSenha;
import com.sptech.school.app_firemanager_notification_service.application.interfaces.saida.EmailSenderInterface;
import com.sptech.school.app_firemanager_notification_service.application.interfaces.saida.RenderizarEmailRecuperacaoSenha;
import com.sptech.school.app_firemanager_notification_service.application.models.Email;
import com.sptech.school.app_firemanager_notification_service.application.models.payload.PayloadRecuperacaoSenha;

public class EnviarNotificacaoRecuperacaoSenhaUseCase implements ExecutarNotificacaoRecuperacaoSenha {

    private static final String ASSUNTO = "[FireManager] - Recuperação de senha";

    private final EmailSenderInterface emailSender;
    private final RenderizarEmailRecuperacaoSenha renderizador;

    public EnviarNotificacaoRecuperacaoSenhaUseCase(
            EmailSenderInterface emailSender,
            RenderizarEmailRecuperacaoSenha renderizador
    ) {
        this.emailSender = emailSender;
        this.renderizador = renderizador;
    }

    @Override
    public void executar(PayloadRecuperacaoSenha payload) {
        String corpoHtml = renderizador.renderizar(payload.linkRedefinicao(), payload.validadeMinutos());
        Email email = new Email(payload.emailDestinatario(), ASSUNTO, corpoHtml);

        emailSender.enviar(email);
    }
}
