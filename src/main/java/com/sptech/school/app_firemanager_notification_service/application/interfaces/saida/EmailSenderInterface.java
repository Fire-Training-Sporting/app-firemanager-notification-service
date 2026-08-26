package com.sptech.school.app_firemanager_notification_service.application.interfaces.saida;

import com.sptech.school.app_firemanager_notification_service.application.models.Email;

public interface EmailSenderInterface {

    void enviar(Email email);
}
