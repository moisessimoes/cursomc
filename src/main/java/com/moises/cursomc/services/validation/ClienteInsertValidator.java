package com.moises.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.moises.cursomc.domain.Cliente;
import com.moises.cursomc.domain.enums.TipoCliente;
import com.moises.cursomc.dto.NovoClienteDTO;
import com.moises.cursomc.repositories.ClienteRepository;
import com.moises.cursomc.resources.exception.FieldMessage;
import com.moises.cursomc.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, NovoClienteDTO> {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	public void initialize(ClienteInsert ann) {}
	
	
	@Override
	public boolean isValid(NovoClienteDTO objDTO, ConstraintValidatorContext context) {
		
		List<FieldMessage> listaDeErros = new ArrayList<>();
		
		//incluir os testes aqui e inserir os erros na lista
		
		if(objDTO.getTipo().equals(TipoCliente.PESSOAFISICA.getCodigo()) && !BR.isValidCPF(objDTO.getCpfOuCnpj())) {
			
			listaDeErros.add(new FieldMessage("cpfOuCnpj", "CPF Inválido!"));
		}
		
		
		if(objDTO.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCodigo()) && !BR.isValidCNPJ(objDTO.getCpfOuCnpj())) {
			
			listaDeErros.add(new FieldMessage("cpfOuCnpj", "CNPJ Inválido!"));
		}
		
		
		Cliente aux = clienteRepository.findByEmail(objDTO.getEmail());
		
		if(aux != null) {
			
			listaDeErros.add(new FieldMessage("email", "E-mail já existente!"));
		}
		
		for (FieldMessage fm : listaDeErros) {
			
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(fm.getMessage()).addPropertyNode(fm.getFieldName()).addConstraintViolation();
		}
		
		return listaDeErros.isEmpty();
	}
}
