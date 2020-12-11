package com.moises.cursomc.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

public class MockEmailService extends AbstractEmailService {
	
	private static final Logger LOG = LoggerFactory.getLogger(MockEmailService.class);

	@Override
	public void sendEmail(SimpleMailMessage msg) {
		
		LOG.info("Simulando Envio de E-mail em Texto Plano...");
		
		LOG.info(msg.toString());
		
		LOG.info("E-mail em Texto Plano Enviado!");
	}
	

	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		
		LOG.info("Simulando Envio de E-mail HTML...");
		
		LOG.info(msg.toString());
		
		LOG.info("E-mail HTML Enviado!");
	}
}
