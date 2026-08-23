package com.sptech.school.app_firemanager_notification_service.application.models.payload;

import com.sptech.school.app_firemanager_notification_service.domain.enums.StatusAgendamento;

import java.time.LocalDate;
import java.time.LocalTime;

public record PayloadAluno(
        Long agendamentoId,
        String nomeProfessor,
        LocalDate data,
        LocalTime hora,
        StatusAgendamento status,
        String emailDestinatario
) {}