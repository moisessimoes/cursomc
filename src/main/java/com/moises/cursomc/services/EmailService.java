package com.moises.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.moises.cursomc.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);

}
