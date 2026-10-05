package com.sptech.school.app_firemanager_notification_service.infrastructure.rabbitmq.configuration;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQInitializer implements ApplicationRunner {

    private final RabbitAdmin rabbitAdmin;

    public RabbitMQInitializer(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
    }

    @Override
    public void run(ApplicationArguments args) {
        int maxTentativas = 10;
        int esperaMs = 3000;

        for (int tentativa = 1; tentativa <= maxTentativas; tentativa++) {
            try {
                rabbitAdmin.initialize();

                System.out.println("RabbitMQ inicializado com sucesso.");
                return;

            } catch (Exception e) {
                System.out.println(
                        "RabbitMQ ainda não está disponível. "
                                + "Tentativa " + tentativa + "/" + maxTentativas
                );

                if (tentativa == maxTentativas) {
                    throw e;
                }

                try {
                    Thread.sleep(esperaMs);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException(
                            "Thread interrompida durante inicialização do RabbitMQ",
                            interruptedException
                    );
                }
            }
        }
    }
}