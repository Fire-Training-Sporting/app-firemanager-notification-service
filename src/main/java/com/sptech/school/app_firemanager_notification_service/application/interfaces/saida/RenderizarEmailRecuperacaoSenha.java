package com.sptech.school.app_firemanager_notification_service.application.interfaces.saida;

public interface RenderizarEmailRecuperacaoSenha {

    String renderizar(String linkRedefinicao, int validadeMinutos);
}
