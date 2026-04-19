package br.com.ecomerce.ecomerce.servicetest;


import static org.mockito.Mockito.*;

import jakarta.mail.internet.MimeMessage;

import br.com.ecomerce.ecomerce.service.SmtpEmailService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class SmtpEmailServiceTest {

    @Mock
    private MailSender mailSender;

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private SmtpEmailService service;

    @Test
    void enviarEmailTextoComSucesso() {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("123@exemplo.com");
        msg.setSubject("Teste");
        msg.setText("Conteúdo do e-mail");
        service.sendEmail(msg);
        verify(mailSender, times(1)).send(msg);
    }

    @Test
    void enviarEmailHtmlComSucesso() {

        MimeMessage mimeMsg = mock(MimeMessage.class);
        service.sendHtmlEmail(mimeMsg);
        verify(javaMailSender, times(1)).send(mimeMsg);
    }
}