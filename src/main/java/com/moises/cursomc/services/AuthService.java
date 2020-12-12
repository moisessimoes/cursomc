package com.moises.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.moises.cursomc.domain.Cliente;
import com.moises.cursomc.repositories.ClienteRepository;
import com.moises.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {
	
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder bcpe;
	
	@Autowired
	private EmailService emailService;
	
	private Random rand = new Random();
	
	
	//===========================================================================================================================
	
	public void sendNewPassword(String email) {
		
		Cliente cliente = clienteRepository.findByEmail(email);
		
		if(cliente == null) throw new ObjectNotFoundException("E-mail não encontrado!");
		
		String newPass = newPassword();
		
		cliente.setSenha(bcpe.encode(newPass));
		
		clienteRepository.save(cliente);
		
		emailService.sendNewPasswordEmail(cliente, newPass);
	}
	
	//===========================================================================================================================

	private String newPassword() { //GERANDO NOVA SENHA PARA O USUARIO
		
		char[] vet = new char[10];
		
		for (int i = 0; i < 10; i++) {
			
			vet[i] = randomChar();
		}
		
		return new String(vet);
	}
	
	//===========================================================================================================================

	private char randomChar() { //CODIGO DOS DIGITOS E LETRAS EM: https://unicode-table.com/pt/
		
		int opcao = rand.nextInt(3);
		
		if(opcao == 0) { //GERA UM DIGITO
			
			return (char) (rand.nextInt(10) + 48); //48 É O CODIGO DO DIGITO ZERO E 10 É A QUANTIDADE DE NUMEROS, OU SEJA, DE 0 A 9.
			
		} else if(opcao == 1) { //GERA LETRA MAIUSCULA
			
			return (char) (rand.nextInt(26) + 65); //48 É O CODIGO DA LETRA 'A'. E 26 É A QUANTIDADE DE LETRAS
			
		} else { //GERA LETRA MINUSCULA
			
			return (char) (rand.nextInt(26) + 97); //48 É O CODIGO DA LETRA 'A'. E 26 É A QUANTIDADE DE LETRAS
		}
	}
}
