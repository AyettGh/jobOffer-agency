package org.example.recrutement.RabbitMQ;

import org.example.recrutement.DTO.UserRegisteredEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routing-key}")
    private String routingKey;

    public void sendUserRegistrationEmail(UserRegisteredEvent event) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, event);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send registration email", e);
        }
    }

    public void sendSimpleEmail(String to, String subject, String body) {
        EmailMessage message = new EmailMessage(to, subject, body);
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    public void sendHtmlEmail(String to, String subject, String templateName, Context context) {
        EmailMessage message = new EmailMessage(to, subject, templateName, context);
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    public static class EmailMessage {
        private String to;
        private String subject;
        private String templateName;
        private String body;
        private Context context;

        public EmailMessage(String to, String subject, String body) {
            this.to = to;
            this.subject = subject;
            this.body = body;
        }

        public EmailMessage(String to, String subject, String templateName, Context context) {
            this.to = to;
            this.subject = subject;
            this.templateName = templateName;
            this.context = context;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getTemplateName() {
            return templateName;
        }

        public void setTemplateName(String templateName) {
            this.templateName = templateName;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

    }
}