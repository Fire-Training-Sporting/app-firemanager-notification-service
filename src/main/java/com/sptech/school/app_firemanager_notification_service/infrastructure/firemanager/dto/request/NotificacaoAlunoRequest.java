package com.sptech.school.app_firemanager_notification_service.infrastructure.firemanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record NotificacaoAlunoRequest(

        @NotNull
        Long agendamentoId,

        @NotBlank
        String nomeProfessor,

        @NotNull
        LocalDate data,

        @NotNull
        LocalTime hora,

        @NotBlank
        String status,

        @NotBlank
        String emailDestinatario
) {}
