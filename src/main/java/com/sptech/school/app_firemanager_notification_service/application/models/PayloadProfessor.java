package com.sptech.school.app_firemanager_notification_service.application.models;

import com.sptech.school.app_firemanager_notification_service.domain.enums.StatusAgendamento;

import java.time.LocalDate;
import java.time.LocalTime;

public record PayloadProfessor(
        Long agendamentoId,
        String nomeAluno,
        String telefoneAluno,
        String nomeCondominio,
        String observacao,
        LocalDate data,
        LocalTime hora,
        StatusAgendamento status,
        String emailDestinatario
) {}
