package com.moises.cursomc.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.moises.cursomc.domain.Cliente;

public class SMTPEmailService extends AbstractEmailService {
	
	private static final Logger LOG = LoggerFactory.getLogger(SMTPEmailService.class);
	
	@Autowired
	private MailSender mailSender;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	//===========================================================================================================================

	@Override
	public void sendEmail(SimpleMailMessage msg) {
		
		LOG.info("Enviando E-mail em Texto Plano...");
		
		mailSender.send(msg); //ENVIANDO O EMAIL
		
		LOG.info("E-mail Enviado!");
		
	}
	
	//===========================================================================================================================

	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		
		LOG.info("Enviando E-mail HTML...");
		
		javaMailSender.send(msg); //ENVIANDO O EMAIL
		
		LOG.info("E-mail Enviado!");
	}

	//===========================================================================================================================
	
	@Override
	public void sendNewPasswordEmail(Cliente cliente, String newPass) {
		// TODO Auto-generated method stub
		
	}
}
