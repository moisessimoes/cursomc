package com.moises.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.moises.cursomc.domain.enums.TipoCliente;
import com.moises.cursomc.dto.NovoClienteDTO;
import com.moises.cursomc.resources.exception.FieldMessage;
import com.moises.cursomc.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, NovoClienteDTO> {
	
	
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
		
		
		
		for (FieldMessage fm : listaDeErros) {
			
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(fm.getMessage()).addPropertyNode(fm.getFieldName()).addConstraintViolation();
		}
		
		return listaDeErros.isEmpty();
	}
}
