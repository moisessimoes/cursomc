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

import com.moises.cursomc.domain.Cliente;
import com.moises.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {
	
	@Value("${default.sender}")
	private String sender;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	//===========================================================================================================================
	
	@Override
	public void sendOrderConfirmationEmail(Pedido obj) { //REFERENTE AO ENVIO DE E-MAIL DE TEXTO DO PEDIDO.
		
		SimpleMailMessage smm = prepareSimpleMailMessageFromPedido(obj);
		
		sendEmail(smm);
	}
	
	//===========================================================================================================================
	
	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido obj) { //REFERENTE AO ENVIO DE E-MAIL DE TEXTO DO PEDIDO.
		
		SimpleMailMessage smm = new SimpleMailMessage();
		
		smm.setTo(obj.getCliente().getEmail()); //Destinatário
		
		smm.setFrom(sender); //Remetente
		
		smm.setSubject("Pedido Confirmado! Código: " + obj.getId());
		
		smm.setSentDate(new Date(System.currentTimeMillis()));
		
		smm.setText(obj.toString());
		
		return smm;
	}
	
	//===========================================================================================================================
	
	protected String htmlFromTemplatePedido(Pedido obj) { //REFERENTE AO ENVIO DE E-MAIL HTML DO PEDIDO.
		
		Context context = new Context();
		
		context.setVariable("pedido", obj);
		
		return templateEngine.process("email/confirmacaoPedido", context);
		
	}
	
	//===========================================================================================================================
	
	@Override
	public void sendOrderConfirmationHtmlEmail(Pedido obj) { //REFERENTE AO ENVIO DE E-MAIL HTML DO PEDIDO.
		
		try {
			
			MimeMessage mm = prepareMimeMessageFromPedido(obj);
			
			sendHtmlEmail(mm);
			
		} catch (MessagingException me) {
			sendOrderConfirmationEmail(obj);
		}
	}
	
	//===========================================================================================================================

	protected MimeMessage prepareMimeMessageFromPedido(Pedido obj) throws MessagingException { //REFERENTE AO ENVIO DE E-MAIL HTML DO PEDIDO.
		
		MimeMessage mm = javaMailSender.createMimeMessage();
		
		MimeMessageHelper mmh = new MimeMessageHelper(mm, true);
		
		mmh.setTo(obj.getCliente().getEmail());
		mmh.setFrom(sender);
		mmh.setSubject("Pedido Confirmado! Código: " + obj.getId());
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		mmh.setText(htmlFromTemplatePedido(obj), true);
		
		return mm;
	}
	
	
	//===========================================================================================================================
	
	@Override
	public void sendNewPasswordEmail(Cliente cliente, String newPass) { //REFERENTE AO ENVIO DE E-MAIL DE TEXTO DE UMA NOVA SENHA DO USUARIO.
		
		SimpleMailMessage smm = prepareNewPasswordEmail(cliente, newPass);
		
		sendEmail(smm);
	}
	

	protected SimpleMailMessage prepareNewPasswordEmail(Cliente cliente, String newPass) { //REFERENTE AO ENVIO DE E-MAIL DE TEXTO DE UMA NOVA SENHA DO USUARIO.
		
		SimpleMailMessage smm = new SimpleMailMessage();
		
		smm.setTo(cliente.getEmail()); //Destinatário
		
		smm.setFrom(sender); //Remetente
		
		smm.setSubject("Solicitação de Nova Senha");
		
		smm.setSentDate(new Date(System.currentTimeMillis()));
		
		smm.setText("NOVA SENHA: " + newPass);
		
		return smm;
	}
}
