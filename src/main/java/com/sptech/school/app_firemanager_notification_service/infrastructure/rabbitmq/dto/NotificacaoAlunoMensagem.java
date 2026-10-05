package com.sptech.school.app_firemanager_notification_service.infrastructure.rabbitmq.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record NotificacaoAlunoMensagem(
        Long agendamentoId,
        String nomeProfessor,
        LocalDate data,
        LocalTime hora,
        String status,
        String emailDestinatario
) {}
