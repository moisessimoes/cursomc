package com.moises.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.moises.cursomc.domain.Cliente;
import com.moises.cursomc.dto.ClienteDTO;
import com.moises.cursomc.repositories.ClienteRepository;
import com.moises.cursomc.services.exceptions.DataIntegrityException;
import com.moises.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	public Cliente find(Integer id) {
		
		Optional<Cliente> obj = clienteRepository.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! ID: " + id + " Tipo: " + Cliente.class.getName()));
	}
	
	
	//Ainda falta o metodo de salvar...
	
	
	
	
	public Cliente update(Cliente obj) {
		
		Cliente newObj = find(obj.getId());
		
		updateData(newObj, obj);
		
		return clienteRepository.save(newObj);
	}
	
	
	private void updateData(Cliente newObj, Cliente obj) {
		
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}


	public void delete(Integer id) {
		
		try {
			clienteRepository.deleteById(id);
			
		} catch (DataIntegrityViolationException dive) {
			throw new DataIntegrityException("Não é possível excluir porque há outras entidades relacionadas.");
		}
	}
	
	
	public List<Cliente> findAll() {
		
		return clienteRepository.findAll();
	}
	
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		
		PageRequest pgR = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return clienteRepository.findAll(pgR);
	}
	
	
	public Cliente fromDTO(ClienteDTO objDTO) {
		
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null);
	}

}
