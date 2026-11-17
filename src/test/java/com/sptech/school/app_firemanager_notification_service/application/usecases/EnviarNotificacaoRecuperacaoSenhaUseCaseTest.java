package com.sptech.school.app_firemanager_notification_service.application.usecases;

import com.sptech.school.app_firemanager_notification_service.application.interfaces.saida.EmailSenderInterface;
import com.sptech.school.app_firemanager_notification_service.application.interfaces.saida.RenderizarEmailRecuperacaoSenha;
import com.sptech.school.app_firemanager_notification_service.application.models.Email;
import com.sptech.school.app_firemanager_notification_service.application.models.payload.PayloadRecuperacaoSenha;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnviarNotificacaoRecuperacaoSenhaUseCaseTest {

    @Mock
    private EmailSenderInterface emailSender;

    @Mock
    private RenderizarEmailRecuperacaoSenha renderizador;

    private EnviarNotificacaoRecuperacaoSenhaUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new EnviarNotificacaoRecuperacaoSenhaUseCase(emailSender, renderizador);
    }

    @Test
    void deveRenderizarEEnviarEmailComOsDadosDaRecuperacao() {
        String linkRedefinicao = "https://app.exemplo.com/reset-password?token=TOKEN";
        PayloadRecuperacaoSenha payload = new PayloadRecuperacaoSenha(
                "usuario@exemplo.com",
                linkRedefinicao,
                30
        );
        when(renderizador.renderizar(linkRedefinicao, 30)).thenReturn("<html>email</html>");

        useCase.executar(payload);

        verify(renderizador).renderizar(linkRedefinicao, 30);
        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
        verify(emailSender).enviar(emailCaptor.capture());

        Email email = emailCaptor.getValue();
        assertThat(email.destinatario()).isEqualTo("usuario@exemplo.com");
        assertThat(email.assunto()).isEqualTo("[FireManager] - Recuperação de senha");
        assertThat(email.corpoMensagem()).isEqualTo("<html>email</html>");
    }
}
