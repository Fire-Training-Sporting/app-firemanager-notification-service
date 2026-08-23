//package com.sptech.school.app_firemanager_notification_service;
//
//import com.sptech.school.app_firemanager_notification_service.application.models.Email;
//import com.sptech.school.app_firemanager_notification_service.infrastructure.springmail.EmailSenderService;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TesteSmtpRunner implements CommandLineRunner {
//
//    private final EmailSenderService emailSenderService;
//
//    public TesteSmtpRunner(EmailSenderService emailSenderService) {
//        this.emailSenderService = emailSenderService;
//    }
//
//    @Override
//    public void run(String... args) {
//        Email email = new Email(
//                "",
//                "Teste de SMTP",
//                "<h1>Funcionou!</h1><p>Se você recebeu isso, o SMTP está configurado corretamente.</p>"
//        );
//
//        emailSenderService.enviar(email);
//        System.out.println("Tentativa de envio disparada.");
//    }
//}