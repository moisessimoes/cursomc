package com.moises.cursomc.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.moises.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {
	
	@Value("${default.sender}")
	private String sender;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
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
	
	
	protected String htmlFromTemplatePedido(Pedido obj) {
		
		Context context = new Context();
		
		context.setVariable("pedido", obj);
		
		return templateEngine.process("email/confirmacaoPedido", context);
		
	}
	
	
	@Override
	public void sendOrderConfirmationHtmlEmail(Pedido obj) {
		
		try {
			
			MimeMessage mm = prepareMimeMessageFromPedido(obj);
			
			sendHtmlEmail(mm);
			
		} catch (MessagingException me) {
			sendOrderConfirmationEmail(obj);
		}
	}


	protected MimeMessage prepareMimeMessageFromPedido(Pedido obj) throws MessagingException {
		
		MimeMessage mm = javaMailSender.createMimeMessage();
		
		MimeMessageHelper mmh = new MimeMessageHelper(mm, true);
		
		mmh.setTo(obj.getCliente().getEmail());
		mmh.setFrom(sender);
		mmh.setSubject("Pedido Confirmado! Código: " + obj.getId());
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		mmh.setText(htmlFromTemplatePedido(obj), true);
		
		return mm;
	}
}
