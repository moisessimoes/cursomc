package com.moises.cursomc.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class SMTPEmailService extends AbstractEmailService {
	
	private static final Logger LOG = LoggerFactory.getLogger(SMTPEmailService.class);
	
	@Autowired
	private MailSender mailSender;

	@Override
	public void sendEmail(SimpleMailMessage msg) {
		
		LOG.info("Enviando E-mail...");
		
		mailSender.send(msg); //ENVIANDO O EMAIL
		
		LOG.info("E-mail Enviado!");
		
	}
}
