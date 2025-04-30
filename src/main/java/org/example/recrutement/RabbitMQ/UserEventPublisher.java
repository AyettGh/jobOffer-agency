package org.example.recrutement.RabbitMQ;

import org.example.recrutement.DTO.UserRegisteredEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public UserEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishUserRegistration(UserRegisteredEvent event) {
        rabbitTemplate.convertAndSend(
                "user.registration.exchange", // on a utlise exchange name
                "user.registered",            // l'utilisation de routing key
                event
        );
    }
}