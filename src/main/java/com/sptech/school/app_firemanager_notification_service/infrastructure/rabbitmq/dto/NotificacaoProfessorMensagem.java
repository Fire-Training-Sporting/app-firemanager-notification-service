package com.sptech.school.app_firemanager_notification_service.infrastructure.rabbitmq.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record NotificacaoProfessorMensagem(
        Long agendamentoId,
        String nomeAluno,
        String telefoneAluno,
        String nomeCondominio,
        String observacao,
        LocalDate data,
        LocalTime hora,
        String status,
        String emailDestinatario
) {}
