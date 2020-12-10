package com.moises.cursomc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.moises.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {
	
	@Value("${default.sender}")
	private String sender;
	
	@Override
	public void sendOrderConfirmationEmail(Pedido obj) {
		
		SimpleMailMessage smm = prepareSimpleMailMessageFromPedido(obj);
		
		sendEmail(smm);
	}

	
	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido obj) {
		
		SimpleMailMessage smm = new SimpleMailMessage();
		
		smm.setTo(obj.getCliente().getEmail()); //Destinatário
		
		smm.setFrom(sender); //Remetente
		
		smm.setSubject("Pedido Confirmado! Código: " + obj.getId());
		
		smm.setSentDate(new Date(System.currentTimeMillis()));
		
		smm.setText(obj.toString());
		
		return smm;
	}
}
