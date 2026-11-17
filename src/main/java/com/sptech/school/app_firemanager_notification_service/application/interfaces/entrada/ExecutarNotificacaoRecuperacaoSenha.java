package com.sptech.school.app_firemanager_notification_service.application.interfaces.entrada;

import com.sptech.school.app_firemanager_notification_service.application.models.payload.PayloadRecuperacaoSenha;

public interface ExecutarNotificacaoRecuperacaoSenha {

    void executar(PayloadRecuperacaoSenha payload);
}
