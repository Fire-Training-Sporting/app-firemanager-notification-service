package com.sptech.school.app_firemanager_notification_service.infrastructure.thymeleaf;

import com.sptech.school.app_firemanager_notification_service.application.interfaces.saida.RenderizarEmailAluno;
import com.sptech.school.app_firemanager_notification_service.application.interfaces.saida.RenderizarEmailProfessor;
import com.sptech.school.app_firemanager_notification_service.domain.models.ConteudoNotificacao;
import com.sptech.school.app_firemanager_notification_service.domain.models.DadosNotificacaoAluno;
import com.sptech.school.app_firemanager_notification_service.domain.models.DadosNotificacaoProfessor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class ThymeleafEmailRender implements RenderizarEmailAluno, RenderizarEmailProfessor {

    private final SpringTemplateEngine template;

    public ThymeleafEmailRender(SpringTemplateEngine template) {
        this.template = template;
    }

    @Override
    public String renderizar(ConteudoNotificacao conteudo, DadosNotificacaoAluno dados) {

        Context context = new Context();
        context.setVariable("mensagem", conteudo.mensagem());

        return template.process("email-aluno", context);
    }

    @Override
    public String renderizar(ConteudoNotificacao conteudo, DadosNotificacaoProfessor dados) {

        Context context = new Context();
        context.setVariable("mensagem", conteudo.mensagem());
        context.setVariable("nomeAluno", dados.nomeAluno());
        context.setVariable("telefone", dados.telefoneAluno());
        context.setVariable("condominio", dados.nomeCondominio());
        context.setVariable("observacao", dados.observacaoOuTraco());
        context.setVariable("data", dados.dataFormatada());
        context.setVariable("hora", dados.horaFormatada());

        return template.process("email-professor", context);
    }
}
