package br.com.ecomerce.ecomerce.service;


import br.com.ecomerce.ecomerce.model.Cliente;
import br.com.ecomerce.ecomerce.model.Pedido;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido pedido);
	
	void sendEmail(SimpleMailMessage msg);

    void sendOrderConfirmationHtmlEmail(Pedido pedido);

    void sendHtmlEmail(MimeMessage msg);
	
	//void sendNewPasswordEmail(Cliente cliente, String newPass);
}
