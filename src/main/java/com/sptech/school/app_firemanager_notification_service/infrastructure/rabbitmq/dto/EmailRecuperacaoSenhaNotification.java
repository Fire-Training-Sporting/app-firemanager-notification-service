package com.sptech.school.app_firemanager_notification_service.infrastructure.rabbitmq.dto;

public record EmailRecuperacaoSenhaNotification(
        String emailDestinatario,
        String linkRedefinicao,
        int validadeMinutos
) {}
