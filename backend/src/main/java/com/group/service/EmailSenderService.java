package com.group.service;
import com.group.dto.SendMessageDTO;
import com.group.util.AsyncThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailSenderService  {

    Logger logger = LoggerFactory.getLogger(EmailSenderService.class);

    private JavaMailSender mailSender;

    private ThreadGroup mailSenderThreadGroup = new ThreadGroup("Mail Sender");

    @Value("${MAIL_USERNAME}")
    private String sender;


    @Autowired
    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public boolean sendMessage(SendMessageDTO messageDto) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(messageDto.address());
            mailMessage.setText(messageDto.message());
            mailMessage.setSubject(messageDto.header());
            mailSender.send(mailMessage);
            logger.info("Email is sent to {}", messageDto.address());
            return true;
        } catch (Exception e) {
            logger.error("Email is not sent to {}", messageDto.address(), e);
        }
        return false;
    }


    public void sendEmail(SendMessageDTO messageDto) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(messageDto.address());
        mailMessage.setSubject(messageDto.header());
        mailMessage.setText(messageDto.message());
        new AsyncThread<SimpleMailMessage>(
                this.mailSenderThreadGroup, messageDto.address(), this::mailSender, mailMessage).start();
    }


    private void mailSender(SimpleMailMessage mailMessage) {
        try {
            mailSender.send(mailMessage);
        } catch (Exception e) {
            logger.error("Email is not sent to {}", mailMessage.getTo(), e);
        }
    }

}
