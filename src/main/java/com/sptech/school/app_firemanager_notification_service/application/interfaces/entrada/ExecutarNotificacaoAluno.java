package com.sptech.school.app_firemanager_notification_service.application.interfaces.entrada;

import com.sptech.school.app_firemanager_notification_service.application.models.payload.PayloadAluno;

public interface ExecutarNotificacaoAluno {

    void executar(PayloadAluno payload);
}
