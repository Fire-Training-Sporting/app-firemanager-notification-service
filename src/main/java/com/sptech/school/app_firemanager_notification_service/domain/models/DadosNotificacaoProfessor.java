package com.sptech.school.app_firemanager_notification_service.domain.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public record DadosNotificacaoProfessor(
        Long agendamentoId,
        String nomeAluno,
        String telefoneAluno,
        String nomeCondominio,
        String observacao,
        LocalDate data,
        LocalTime hora
) {
    private static final DateTimeFormatter DATA_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public String dataFormatada() {
        return data.format(DATA_FMT);
    }

    public String horaFormatada() {
        return hora.format(HORA_FMT);
    }

    public String observacaoOuTraco() {
        return (observacao == null || observacao.isBlank()) ? "-" : observacao;
    }
}