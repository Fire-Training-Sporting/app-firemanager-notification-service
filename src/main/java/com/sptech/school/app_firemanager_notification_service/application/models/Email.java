package com.sptech.school.app_firemanager_notification_service.application.models;

public record Email(
        String destinatario,
        String assunto,
        String corpoMensagem
) {}
