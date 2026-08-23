package com.sptech.school.app_firemanager_notification_service;

import com.sptech.school.app_firemanager_notification_service.application.models.payload.PayloadAluno;
import com.sptech.school.app_firemanager_notification_service.application.usecases.EnviarNotificacaoAlunoUseCase;
import com.sptech.school.app_firemanager_notification_service.domain.enums.StatusAgendamento;
import com.sptech.school.app_firemanager_notification_service.domain.service.ConteudoNotificacaoBuilder;
import com.sptech.school.app_firemanager_notification_service.infrastructure.springmail.EmailSenderService;
import com.sptech.school.app_firemanager_notification_service.infrastructure.thymeleaf.ThymeleafEmailRender;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class TesteSmtpRunner implements CommandLineRunner {

    private final EmailSenderService emailSenderService;
    private final ThymeleafEmailRender thymeleafEmailRender;

    public TesteSmtpRunner(EmailSenderService emailSenderService, ThymeleafEmailRender thymeleafEmailRender) {
        this.emailSenderService = emailSenderService;
        this.thymeleafEmailRender = thymeleafEmailRender;
    }

    @Override
    public void run(String... args) {
        EnviarNotificacaoAlunoUseCase useCase = new EnviarNotificacaoAlunoUseCase(
                new ConteudoNotificacaoBuilder(),
                emailSenderService,
                thymeleafEmailRender
        );

        PayloadAluno payload = new PayloadAluno(
                1L,
                "Professor Teste",
                LocalDate.now().plusDays(3),
                LocalTime.of(14, 0),
                StatusAgendamento.PENDENTE,
                ""
        );

        useCase.executar(payload);
        System.out.println("Tentativa de envio disparada (fluxo completo com Thymeleaf).");
    }
}