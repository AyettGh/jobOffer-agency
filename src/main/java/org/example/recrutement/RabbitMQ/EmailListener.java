package org.example.recrutement.RabbitMQ;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
public class EmailListener {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void processEmail(EmailService.EmailMessage message) {
        try {
            if (message.getTemplateName() != null) {
                String htmlContent = templateEngine.process(message.getTemplateName(), message.getContext());
                sendHtmlEmail(message.getTo(), message.getSubject(), htmlContent);
            } else {
                sendSimpleEmail(message.getTo(), message.getSubject(), message.getBody());
            }
        } catch (Exception e) {
            System.err.println("Failed to process email: " + e.getMessage());
        }
    }

    private void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        mailSender.send(mailMessage);
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, "UTF-8");

        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(mailMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }
}