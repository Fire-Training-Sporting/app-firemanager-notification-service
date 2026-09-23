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
        rabbitAdmin.initialize();
    }
}