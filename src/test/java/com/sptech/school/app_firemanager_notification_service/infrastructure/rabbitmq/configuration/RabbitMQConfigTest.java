package com.sptech.school.app_firemanager_notification_service.infrastructure.rabbitmq.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

import static org.assertj.core.api.Assertions.assertThat;

class RabbitMQConfigTest {

    @Test
    void deveDeclararFilaDuravelEBindingDeRecuperacaoDeSenha() {
        RabbitMQConfig config = new RabbitMQConfig();
        Queue queue = config.filaNotificacaoRecuperacaoSenha();
        DirectExchange exchange = config.exchangeNotificacao();
        Binding binding = config.bindingRecuperacaoSenha(queue, exchange);

        assertThat(exchange.getName()).isEqualTo("exchange.notificacao");
        assertThat(queue.getName()).isEqualTo("fila.notificacao.recuperacao-senha");
        assertThat(queue.isDurable()).isTrue();
        assertThat(binding.getDestination()).isEqualTo(queue.getName());
        assertThat(binding.getExchange()).isEqualTo(exchange.getName());
        assertThat(binding.getRoutingKey()).isEqualTo("notificacao.recuperacao-senha");
    }
}
