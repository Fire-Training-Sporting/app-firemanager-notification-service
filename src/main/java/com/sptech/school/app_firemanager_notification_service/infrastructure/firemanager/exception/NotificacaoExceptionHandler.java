package com.sptech.school.app_firemanager_notification_service.infrastructure.firemanager.exception;

import com.sptech.school.app_firemanager_notification_service.infrastructure.firemanager.dto.response.NotificacaoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotificacaoExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<NotificacaoResponse> tratarPayloadInvalido(MethodArgumentNotValidException e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new NotificacaoResponse("Payload invalido para execucao."));
    }
}
