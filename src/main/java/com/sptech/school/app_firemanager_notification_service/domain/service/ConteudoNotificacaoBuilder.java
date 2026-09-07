package com.sptech.school.app_firemanager_notification_service.domain.service;

import com.sptech.school.app_firemanager_notification_service.domain.enums.StatusAgendamento;
import com.sptech.school.app_firemanager_notification_service.domain.models.ConteudoNotificacao;
import com.sptech.school.app_firemanager_notification_service.domain.models.DadosNotificacaoAluno;
import com.sptech.school.app_firemanager_notification_service.domain.models.DadosNotificacaoProfessor;

public class ConteudoNotificacaoBuilder {

    public ConteudoNotificacao buildParaAluno(StatusAgendamento status, DadosNotificacaoAluno dados) {

        return switch (status) {
            case PENDENTE -> new ConteudoNotificacao(
                    "[FireManager] - Nova Aula Agendada!",
                    String.format("Você tem uma nova aula agendada com %s no dia %s às %s. [ID Agendamento: %d]",
                            dados.nomeProfessor(), dados.dataFormatada(), dados.horaFormatada(), dados.agendamentoId())
            );
            case CANCELADO -> new ConteudoNotificacao(
                    "[FireManager] - Agendamento Cancelado",
                    String.format("Seu agendamento foi cancelado. [ID Agendamento: %d]",
                            dados.agendamentoId())
            );
            case CONFIRMADO -> new ConteudoNotificacao(
                    "[FireManager] - Aula Confirmada",
                    String.format("Sua aula com %s é daqui menos de 24h! [ID Agendamento: %d]",
                            dados.nomeProfessor(), dados.agendamentoId())
            );
            case FINALIZADO -> new ConteudoNotificacao(
                    "[FireManager] - Aula Concluída",
                    String.format("Sua aula com %s foi concluída. Obrigado pela confiança em nosso serviço! [ID Agendamento: %d]",
                            dados.nomeProfessor(), dados.agendamentoId())
            );
        };
    }

    public ConteudoNotificacao buildParaProfessor(StatusAgendamento status, DadosNotificacaoProfessor dados) {

        return switch (status) {
            case PENDENTE -> new ConteudoNotificacao(
                    "[FireManager] - Nova Aula Agendada!",
                    String.format("Você tem uma nova aula agendada com %s no dia %s às %s. [ID Agendamento: %d]",
                            dados.nomeAluno(), dados.dataFormatada(), dados.horaFormatada(), dados.agendamentoId())
            );
            case CANCELADO -> new ConteudoNotificacao(
                    "[FireManager] - Agendamento Cancelado",
                    String.format("O agendamento com %s no dia %s às %s foi cancelado. [ID Agendamento: %d]",
                            dados.nomeAluno(), dados.dataFormatada(), dados.horaFormatada(), dados.agendamentoId())
            );
            case CONFIRMADO -> new ConteudoNotificacao(
                    "[FireManager] - Aula Confirmada",
                    String.format("A aula com %s é daqui menos de 24h e não pode mais ser cancelada. [ID Agendamento: %d]",
                            dados.nomeAluno(), dados.agendamentoId())
            );
            case FINALIZADO -> new ConteudoNotificacao(
                    "[FireManager] - Aula Concluída",
                    String.format("A aula com %s realizada em %s às %s foi concluída. [ID Agendamento: %d]",
                            dados.nomeAluno(), dados.dataFormatada(), dados.horaFormatada(), dados.agendamentoId())
            );
        };
    }
}
