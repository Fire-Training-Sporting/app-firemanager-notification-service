package com.sptech.school.app_firemanager_notification_service.infrastructure.springmail;

import com.sptech.school.app_firemanager_notification_service.application.interfaces.saida.EmailSenderInterface;
import com.sptech.school.app_firemanager_notification_service.application.models.Email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService implements EmailSenderInterface {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailRemetente;

    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    @Async
    public void enviar(Email email) {

        try {
            MimeMessage mensagem = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");

            helper.setFrom(emailRemetente);
            helper.setTo(email.destinatario());
            helper.setSubject(email.assunto());
            helper.setText(email.corpoMensagem(), true);

            javaMailSender.send(mensagem);
        } catch (MessagingException e) {
            System.out.println(e);
        }
    }
}
