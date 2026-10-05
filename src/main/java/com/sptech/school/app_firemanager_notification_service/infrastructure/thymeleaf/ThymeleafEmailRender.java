package com.sptech.school.app_firemanager_notification_service.infrastructure.thymeleaf;

import com.sptech.school.app_firemanager_notification_service.application.interfaces.saida.RenderizarEmailAluno;
import com.sptech.school.app_firemanager_notification_service.application.interfaces.saida.RenderizarEmailProfessor;
import com.sptech.school.app_firemanager_notification_service.domain.models.ConteudoNotificacao;
import com.sptech.school.app_firemanager_notification_service.domain.models.DadosNotificacaoAluno;
import com.sptech.school.app_firemanager_notification_service.domain.models.DadosNotificacaoProfessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class ThymeleafEmailRender implements RenderizarEmailAluno, RenderizarEmailProfessor {

    private final SpringTemplateEngine template;
    private static final Logger log = LoggerFactory.getLogger(ThymeleafEmailRender.class);

    public ThymeleafEmailRender(SpringTemplateEngine template) {
        this.template = template;
    }

    @Override
    public String renderizar(ConteudoNotificacao conteudo, DadosNotificacaoAluno dados) {

        try {
            Context context = new Context();
            context.setVariable("mensagem", conteudo.mensagem());

            return template.process("email-aluno", context);
        } catch (Exception e) {
            log.error("Falha ao renderizar template 'email-aluno' - agendamentoId={}", dados.agendamentoId(), e);
            throw e;
        }
    }

    @Override
    public String renderizar(ConteudoNotificacao conteudo, DadosNotificacaoProfessor dados) {

        try {
            Context context = new Context();
            context.setVariable("mensagem", conteudo.mensagem());
            context.setVariable("nomeAluno", dados.nomeAluno());
            context.setVariable("telefone", dados.telefoneAluno());
            context.setVariable("condominio", dados.nomeCondominio());
            context.setVariable("observacao", dados.observacaoOuTraco());
            context.setVariable("data", dados.dataFormatada());
            context.setVariable("hora", dados.horaFormatada());

            return template.process("email-professor", context);
        } catch (Exception e) {
            log.error("Falha ao renderizar template 'email-professor' - agendamentoId={}", dados.agendamentoId(), e);
            throw e;
        }
    }
}
