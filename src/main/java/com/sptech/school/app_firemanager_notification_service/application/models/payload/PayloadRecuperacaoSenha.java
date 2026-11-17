package com.sptech.school.app_firemanager_notification_service.application.models.payload;

public record PayloadRecuperacaoSenha(
        String emailDestinatario,
        String linkRedefinicao,
        int validadeMinutos
) {}
