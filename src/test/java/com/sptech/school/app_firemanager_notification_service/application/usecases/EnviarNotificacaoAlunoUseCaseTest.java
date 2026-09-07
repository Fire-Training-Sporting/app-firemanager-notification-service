package com.sptech.school.app_firemanager_notification_service.application.usecases;

import com.sptech.school.app_firemanager_notification_service.application.interfaces.saida.EmailSenderInterface;
import com.sptech.school.app_firemanager_notification_service.application.interfaces.saida.RenderizarEmailAluno;
import com.sptech.school.app_firemanager_notification_service.application.models.Email;
import com.sptech.school.app_firemanager_notification_service.application.models.payload.PayloadAluno;
import com.sptech.school.app_firemanager_notification_service.domain.enums.StatusAgendamento;
import com.sptech.school.app_firemanager_notification_service.domain.models.ConteudoNotificacao;
import com.sptech.school.app_firemanager_notification_service.domain.models.DadosNotificacaoAluno;
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
class EnviarNotificacaoAlunoUseCaseTest {

    @Mock
    private ConteudoNotificacaoBuilder conteudoNotificacaoBuilder;

    @Mock
    private EmailSenderInterface emailSender;

    @Mock
    private RenderizarEmailAluno renderizador;

    private EnviarNotificacaoAlunoUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new EnviarNotificacaoAlunoUseCase(conteudoNotificacaoBuilder, emailSender, renderizador);
    }

    @Test
    void deveTraduzirPayloadEDelegarParaDominioInfraCorretamente() {

        // arrange
        PayloadAluno payload = new PayloadAluno(
                42L,
                "Professor João",
                LocalDate.of(2026, 8, 30),
                LocalTime.of(14, 0),
                StatusAgendamento.PENDENTE,
                "aluno@example.com"
        );

        ConteudoNotificacao conteudoEsperado = new ConteudoNotificacao(
                "[FireManager] - Nova Aula Agendada!",
                "Você tem uma nova aula agendada..."
        );

        when(conteudoNotificacaoBuilder.buildParaAluno(eq(StatusAgendamento.PENDENTE), any(DadosNotificacaoAluno.class)))
                .thenReturn(conteudoEsperado);

        when(renderizador.renderizar(eq(conteudoEsperado), any(DadosNotificacaoAluno.class)))
                .thenReturn("<html>corpo renderizado</html>");

        // act
        useCase.executar(payload);

        // assert: o domínio foi chamado com os dados corretos, traduzidos do payload
        ArgumentCaptor<DadosNotificacaoAluno> dadosCaptor = ArgumentCaptor.forClass(DadosNotificacaoAluno.class);
        verify(conteudoNotificacaoBuilder).buildParaAluno(eq(StatusAgendamento.PENDENTE), dadosCaptor.capture());

        DadosNotificacaoAluno dadosCapturados = dadosCaptor.getValue();
        assertThat(dadosCapturados.agendamentoId()).isEqualTo(42L);
        assertThat(dadosCapturados.nomeProfessor()).isEqualTo("Professor João");
        assertThat(dadosCapturados.data()).isEqualTo(LocalDate.of(2026, 8, 30));
        assertThat(dadosCapturados.hora()).isEqualTo(LocalTime.of(14, 0));

        // assert: o renderizador foi chamado com o conteúdo devolvido pelo domínio
        verify(renderizador).renderizar(conteudoEsperado, dadosCapturados);

        // assert: o email final foi montado e enviado com os dados certos
        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
        verify(emailSender).enviar(emailCaptor.capture());

        Email emailEnviado = emailCaptor.getValue();
        assertThat(emailEnviado.destinatario()).isEqualTo("aluno@example.com");
        assertThat(emailEnviado.assunto()).isEqualTo("[FireManager] - Nova Aula Agendada!");
        assertThat(emailEnviado.corpoMensagem()).isEqualTo("<html>corpo renderizado</html>");
    }
}