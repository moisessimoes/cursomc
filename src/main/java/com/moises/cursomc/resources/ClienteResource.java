package com.moises.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.moises.cursomc.domain.Cliente;
import com.moises.cursomc.dto.ClienteDTO;
import com.moises.cursomc.dto.NovoClienteDTO;
import com.moises.cursomc.services.ClienteService;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {
	
	@Autowired
	private ClienteService service;
	
	//===========================================================================================================================
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Cliente> find(@PathVariable Integer id) {
		
		Cliente obj = service.find(id);
		
		return ResponseEntity.ok().body(obj);
	}
	
	//===========================================================================================================================
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> inserir(@Valid @RequestBody NovoClienteDTO objDTO) {
		
		Cliente obj = service.fromDTO(objDTO);
		
		obj = service.save(obj);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	//===========================================================================================================================
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO objDTO, @PathVariable Integer id) {
		
		Cliente obj = service.fromDTO(objDTO);
		
		obj.setId(id); //Informando o ID do objeto que vai ser atualizado.
		
		obj = service.update(obj);
		
		return ResponseEntity.noContent().build();
	}
	
	//===========================================================================================================================
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		
		service.delete(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ClienteDTO>> findAll() {
		
		 List<Cliente> lista = service.findAll();
		 
		 List<ClienteDTO> listaDTO = lista.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(listaDTO);
	}
	
	//===========================================================================================================================
	
	//METODO QUE RETORNA OS DADOS POR PAGINAÇÃO
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<ClienteDTO>> findPage(@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage, 
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy, 
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		
		Page<Cliente> lista = service.findPage(page, linesPerPage, orderBy, direction);
		
		Page<ClienteDTO> listaDTO = lista.map(obj -> new ClienteDTO(obj));
		
		return ResponseEntity.ok().body(listaDTO);
	}
}
