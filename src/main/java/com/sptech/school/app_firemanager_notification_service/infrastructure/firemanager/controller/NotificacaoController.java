package com.sptech.school.app_firemanager_notification_service.infrastructure.firemanager.controller;

import com.sptech.school.app_firemanager_notification_service.application.interfaces.entrada.ExecutarNotificacaoAluno;
import com.sptech.school.app_firemanager_notification_service.application.interfaces.entrada.ExecutarNotificacaoProfessor;
import com.sptech.school.app_firemanager_notification_service.application.models.payload.PayloadAluno;
import com.sptech.school.app_firemanager_notification_service.application.models.payload.PayloadProfessor;
import com.sptech.school.app_firemanager_notification_service.domain.enums.StatusAgendamento;
import com.sptech.school.app_firemanager_notification_service.infrastructure.firemanager.dto.request.NotificacaoAlunoRequest;
import com.sptech.school.app_firemanager_notification_service.infrastructure.firemanager.dto.request.NotificacaoProfessorRequest;
import com.sptech.school.app_firemanager_notification_service.infrastructure.firemanager.dto.response.NotificacaoResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/notificacao")
public class NotificacaoController {

    private final ExecutarNotificacaoAluno notificacaoAluno;
    private final ExecutarNotificacaoProfessor notificacaoProfessor;

    public NotificacaoController(ExecutarNotificacaoAluno notificacaoAluno, ExecutarNotificacaoProfessor notificacaoProfessor) {
        this.notificacaoAluno = notificacaoAluno;
        this.notificacaoProfessor = notificacaoProfessor;
    }


    @PostMapping("/email/aluno")
    public ResponseEntity<NotificacaoResponse> executarNotificacaoAluno(@Valid @RequestBody NotificacaoAlunoRequest request) {

        StatusAgendamento status = StatusAgendamento.valueOf(request.status().toUpperCase());

        PayloadAluno payload = new PayloadAluno(
                request.agendamentoId(),
                request.nomeProfessor(),
                request.data(),
                request.hora(),
                status,
                request.emailDestinatario()
        );

        notificacaoAluno.executar(payload);

        return ResponseEntity.accepted().body(new NotificacaoResponse("Execucao de notificacao para aluno iniciada"));
    }

    @PostMapping("/email/professor")
    public ResponseEntity<NotificacaoResponse> executarNotificacaoProfessor(@Valid @RequestBody NotificacaoProfessorRequest request) {

        StatusAgendamento status = StatusAgendamento.valueOf(request.status().toUpperCase());

        PayloadProfessor payload = new PayloadProfessor(
                request.agendamentoId(),
                request.nomeAluno(),
                request.telefoneAluno(),
                request.nomeCondominio(),
                request.observacao(),
                request.data(),
                request.hora(),
                status,
                request.emailDestinatario()
        );

        notificacaoProfessor.executar(payload);

        return ResponseEntity.accepted().body(new NotificacaoResponse("Execucao de notificacao para professor iniciada"));
    }
}
