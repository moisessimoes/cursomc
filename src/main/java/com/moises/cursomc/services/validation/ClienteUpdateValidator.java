package com.moises.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.moises.cursomc.domain.Cliente;
import com.moises.cursomc.dto.ClienteDTO;
import com.moises.cursomc.repositories.ClienteRepository;
import com.moises.cursomc.resources.exception.FieldMessage;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	public void initialize(ClienteUpdate ann) {}
	
	
	@Override
	public boolean isValid(ClienteDTO objDTO, ConstraintValidatorContext context) {
		
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer uriID = Integer.parseInt(map.get("id"));
		
		List<FieldMessage> listaDeErros = new ArrayList<>();
		
		Cliente aux = clienteRepository.findByEmail(objDTO.getEmail());
		
		if(aux != null && !aux.getId().equals(uriID)) {
			
			listaDeErros.add(new FieldMessage("email", "E-mail já existente!"));
		}
		
		for (FieldMessage fm : listaDeErros) {
			
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(fm.getMessage()).addPropertyNode(fm.getFieldName()).addConstraintViolation();
		}
		
		return listaDeErros.isEmpty();
	}
}
