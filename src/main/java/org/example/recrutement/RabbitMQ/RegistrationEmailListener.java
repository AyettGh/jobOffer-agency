package org.example.recrutement.RabbitMQ;

import jakarta.mail.internet.MimeMessage;
import org.example.recrutement.DTO.UserRegisteredEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
public class RegistrationEmailListener {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @RabbitListener(queues = "${spring.rabbitmq.registration-queue}")

    public void handleRegistration(UserRegisteredEvent event) {
        System.out.println("Received event for: " + event.getEmail());

        try {
            Context context = new Context();
            context.setVariable("name", event.getUsername());

            String htmlContent = templateEngine.process("email/welcome", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(event.getEmail());
            helper.setSubject("Welcome to Our Platform");
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }
    }
}