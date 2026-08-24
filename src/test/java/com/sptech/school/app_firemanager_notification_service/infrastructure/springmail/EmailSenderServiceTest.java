package com.sptech.school.app_firemanager_notification_service.infrastructure.springmail;

import com.sptech.school.app_firemanager_notification_service.application.models.Email;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailSenderServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    private EmailSenderService emailSenderService;

    @BeforeEach
    void setUp() {
        emailSenderService = new EmailSenderService(javaMailSender, "luan.teste@gmail.com");
    }

    @Test
    void deveMontarEEnviarEmailComDadosCorretos() throws Exception {
        MimeMessage mimeMessageReal = new MimeMessage(Session.getDefaultInstance(new Properties()));
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessageReal);

        Email email = new Email("aluno@example.com", "Assunto de teste", "<p>corpo</p>");

        emailSenderService.enviar(email);

        verify(javaMailSender).send(mimeMessageReal);
        assertThat(mimeMessageReal.getSubject()).isEqualTo("Assunto de teste");
        assertThat(mimeMessageReal.getAllRecipients()[0].toString()).isEqualTo("aluno@example.com");
        assertThat(mimeMessageReal.getFrom()[0].toString()).isEqualTo("luan.teste@gmail.com");
    }

    @Test
    void naoDeveEnviarQuandoDestinatarioForInvalido() throws Exception {
        MimeMessage mimeMessageReal = new MimeMessage(Session.getDefaultInstance(new Properties()));
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessageReal);

        Email emailInvalido = new Email("isso não é um email", "Assunto", "corpo");

        emailSenderService.enviar(emailInvalido);

        verify(javaMailSender, never()).send(any(MimeMessage.class));
    }
}