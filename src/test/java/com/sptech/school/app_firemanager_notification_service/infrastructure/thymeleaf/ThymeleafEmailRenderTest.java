package com.sptech.school.app_firemanager_notification_service.infrastructure.thymeleaf;

import com.sptech.school.app_firemanager_notification_service.domain.models.ConteudoNotificacao;
import com.sptech.school.app_firemanager_notification_service.domain.models.DadosNotificacaoAluno;
import com.sptech.school.app_firemanager_notification_service.domain.models.DadosNotificacaoProfessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ThymeleafEmailRendererTest {

    @Mock
    private SpringTemplateEngine templateEngine;

    private ThymeleafEmailRender renderer;

    @BeforeEach
    void setUp() {
        renderer = new ThymeleafEmailRender(templateEngine);
    }

    @Test
    void deveRenderizarTemplateDeAlunoComMensagemCorreta() {
        when(templateEngine.process(eq("email-aluno"), any(Context.class)))
                .thenReturn("<html>renderizado aluno</html>");

        ConteudoNotificacao conteudo = new ConteudoNotificacao("Assunto", "Mensagem para o aluno");
        DadosNotificacaoAluno dados = new DadosNotificacaoAluno(1L, "Prof", LocalDate.now(), LocalTime.now());

        String resultado = renderer.renderizar(conteudo, dados);

        assertThat(resultado).isEqualTo("<html>renderizado aluno</html>");

        ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
        verify(templateEngine).process(eq("email-aluno"), contextCaptor.capture());
        assertThat(contextCaptor.getValue().getVariable("mensagem")).isEqualTo("Mensagem para o aluno");
    }

    @Test
    void deveRenderizarTemplateDeProfessorComTodasAsVariaveis() {
        when(templateEngine.process(eq("email-professor"), any(Context.class)))
                .thenReturn("<html>renderizado professor</html>");

        ConteudoNotificacao conteudo = new ConteudoNotificacao("Assunto", "Mensagem para o professor");
        DadosNotificacaoProfessor dados = new DadosNotificacaoProfessor(
                1L, "Maria", "11999990000", "Condomínio Sol", "Obs",
                LocalDate.of(2026, 8, 30), LocalTime.of(14, 0)
        );

        String resultado = renderer.renderizar(conteudo, dados);

        assertThat(resultado).isEqualTo("<html>renderizado professor</html>");

        ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
        verify(templateEngine).process(eq("email-professor"), contextCaptor.capture());
        Context context = contextCaptor.getValue();

        assertThat(context.getVariable("mensagem")).isEqualTo("Mensagem para o professor");
        assertThat(context.getVariable("nomeAluno")).isEqualTo("Maria");
        assertThat(context.getVariable("telefone")).isEqualTo("11999990000");
        assertThat(context.getVariable("condominio")).isEqualTo("Condomínio Sol");
        assertThat(context.getVariable("observacao")).isEqualTo("Obs");
        assertThat(context.getVariable("data")).isEqualTo("30/08/2026");
        assertThat(context.getVariable("hora")).isEqualTo("14:00");
    }
}