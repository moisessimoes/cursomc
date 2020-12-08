package com.moises.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.moises.cursomc.domain.Categoria;
import com.moises.cursomc.dto.CategoriaDTO;
import com.moises.cursomc.repositories.CategoriaRepository;
import com.moises.cursomc.services.exceptions.DataIntegrityException;
import com.moises.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Categoria find(Integer id) {
		
		Optional<Categoria> obj = categoriaRepository.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! ID: " + id + " Tipo: " + Categoria.class.getName()));
	}
	
	
	/*O metodo do Spring Data para salvar e atualizar um objeto é o mesmo, logo, para atualizar, é
	 * 
	 * necessário informar o ID do objeto que vai ser atualizado.*/
	
	
	public Categoria save(Categoria obj) {
		
		obj.setId(null);
		
		return categoriaRepository.save(obj);
	}
	
	
	public Categoria update(Categoria obj) {
		
		Categoria newObj = find(obj.getId());
		
		updateData(newObj, obj);
		
		return categoriaRepository.save(newObj);
	}
	
	
	private void updateData(Categoria newObj, Categoria obj) {
		
		newObj.setNome(obj.getNome());
	}


	public void delete(Integer id) {
		
		try {
			categoriaRepository.deleteById(id);
			
		} catch (DataIntegrityViolationException dive) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos associados a ela.");
		}
	}
	
	
	public List<Categoria> findAll() {
		
		return categoriaRepository.findAll();
	}
	
	
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		
		PageRequest pgR = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return categoriaRepository.findAll(pgR);
	}
	
	
	public Categoria fromDTO(CategoriaDTO objDTO) {
		
		return new Categoria(objDTO.getId(), objDTO.getNome());
	}
}
