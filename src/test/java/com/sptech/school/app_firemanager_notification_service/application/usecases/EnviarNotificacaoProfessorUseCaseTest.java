package com.sptech.school.app_firemanager_notification_service.application.usecases;

import com.sptech.school.app_firemanager_notification_service.application.interfaces.saida.EmailSenderInterface;
import com.sptech.school.app_firemanager_notification_service.application.interfaces.saida.RenderizarEmailProfessor;
import com.sptech.school.app_firemanager_notification_service.application.models.Email;
import com.sptech.school.app_firemanager_notification_service.application.models.payload.PayloadProfessor;
import com.sptech.school.app_firemanager_notification_service.domain.enums.StatusAgendamento;
import com.sptech.school.app_firemanager_notification_service.domain.models.ConteudoNotificacao;
import com.sptech.school.app_firemanager_notification_service.domain.models.DadosNotificacaoProfessor;
import com.sptech.school.app_firemanager_notification_service.domain.service.ConteudoNotificacaoBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnviarNotificacaoProfessorUseCaseTest {

    @Mock
    private ConteudoNotificacaoBuilder conteudoNotificacaoBuilder;

    @Mock
    private EmailSenderInterface emailSender;

    @Mock
    private RenderizarEmailProfessor renderizador;

    private EnviarNotificacaoProfessorUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new EnviarNotificacaoProfessorUseCase(conteudoNotificacaoBuilder, emailSender, renderizador);
    }

    @Test
    void deveTraduzirPayloadEDelegarParaDominioInfraCorretamente() {
        // arrange
        PayloadProfessor payload = new PayloadProfessor(
                42L,
                "Maria Aluna",
                "11999990000",
                "Condomínio Sol",
                "Levar tatame",
                LocalDate.of(2026, 8, 30),
                LocalTime.of(14, 0),
                StatusAgendamento.CANCELADO,
                "professor@example.com"
        );

        ConteudoNotificacao conteudoEsperado = new ConteudoNotificacao(
                "[FireManager] - Agendamento Cancelado",
                "O agendamento foi cancelado..."
        );

        when(conteudoNotificacaoBuilder.buildParaProfessor(eq(StatusAgendamento.CANCELADO), any(DadosNotificacaoProfessor.class)))
                .thenReturn(conteudoEsperado);

        when(renderizador.renderizar(eq(conteudoEsperado), any(DadosNotificacaoProfessor.class)))
                .thenReturn("<html>corpo renderizado professor</html>");

        // act
        useCase.executar(payload);

        // assert: dados traduzidos corretamente do payload
        ArgumentCaptor<DadosNotificacaoProfessor> dadosCaptor = ArgumentCaptor.forClass(DadosNotificacaoProfessor.class);
        verify(conteudoNotificacaoBuilder).buildParaProfessor(eq(StatusAgendamento.CANCELADO), dadosCaptor.capture());

        DadosNotificacaoProfessor dadosCapturados = dadosCaptor.getValue();
        assertThat(dadosCapturados.agendamentoId()).isEqualTo(42L);
        assertThat(dadosCapturados.nomeAluno()).isEqualTo("Maria Aluna");
        assertThat(dadosCapturados.telefoneAluno()).isEqualTo("11999990000");
        assertThat(dadosCapturados.nomeCondominio()).isEqualTo("Condomínio Sol");
        assertThat(dadosCapturados.observacao()).isEqualTo("Levar tatame");
        assertThat(dadosCapturados.data()).isEqualTo(LocalDate.of(2026, 8, 30));
        assertThat(dadosCapturados.hora()).isEqualTo(LocalTime.of(14, 0));

        // assert: renderizador chamado com o conteúdo certo
        verify(renderizador).renderizar(conteudoEsperado, dadosCapturados);

        // assert: email final montado e enviado corretamente
        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
        verify(emailSender).enviar(emailCaptor.capture());

        Email emailEnviado = emailCaptor.getValue();
        assertThat(emailEnviado.destinatario()).isEqualTo("professor@example.com");
        assertThat(emailEnviado.assunto()).isEqualTo("[FireManager] - Agendamento Cancelado");
        assertThat(emailEnviado.corpoMensagem()).isEqualTo("<html>corpo renderizado professor</html>");
    }
}