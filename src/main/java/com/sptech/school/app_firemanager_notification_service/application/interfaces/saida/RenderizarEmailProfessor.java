package com.sptech.school.app_firemanager_notification_service.application.interfaces.saida;

import com.sptech.school.app_firemanager_notification_service.domain.models.ConteudoNotificacao;
import com.sptech.school.app_firemanager_notification_service.domain.models.DadosNotificacaoProfessor;

public interface RenderizarEmailProfessor {

    String renderizar(ConteudoNotificacao conteudo, DadosNotificacaoProfessor dados);
}
