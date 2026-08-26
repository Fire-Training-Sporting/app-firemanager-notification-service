package com.sptech.school.app_firemanager_notification_service.infrastructure.configuration;

import com.sptech.school.app_firemanager_notification_service.application.interfaces.entrada.ExecutarNotificacaoAluno;
import com.sptech.school.app_firemanager_notification_service.application.interfaces.entrada.ExecutarNotificacaoProfessor;
import com.sptech.school.app_firemanager_notification_service.application.interfaces.saida.EmailSenderInterface;
import com.sptech.school.app_firemanager_notification_service.application.interfaces.saida.RenderizarEmailAluno;
import com.sptech.school.app_firemanager_notification_service.application.interfaces.saida.RenderizarEmailProfessor;
import com.sptech.school.app_firemanager_notification_service.application.usecases.EnviarNotificacaoAlunoUseCase;
import com.sptech.school.app_firemanager_notification_service.application.usecases.EnviarNotificacaoProfessorUseCase;
import com.sptech.school.app_firemanager_notification_service.domain.service.ConteudoNotificacaoBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public ConteudoNotificacaoBuilder conteudoNotificacaoBuilder() {
        return new ConteudoNotificacaoBuilder();
    }

    @Bean
    public ExecutarNotificacaoAluno executarNotificacaoAluno(
            ConteudoNotificacaoBuilder builder,
            EmailSenderInterface sender,
            RenderizarEmailAluno renderizador) {

        return new EnviarNotificacaoAlunoUseCase(builder, sender, renderizador);
    }

    @Bean
    public ExecutarNotificacaoProfessor executarNotificacaoProfessor(
            ConteudoNotificacaoBuilder builder,
            EmailSenderInterface sender,
            RenderizarEmailProfessor renderizador
    ) {

        return new EnviarNotificacaoProfessorUseCase(builder, sender, renderizador);
    }
}
