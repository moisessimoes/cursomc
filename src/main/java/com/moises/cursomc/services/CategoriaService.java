package com.moises.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.moises.cursomc.domain.Categoria;
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
	
	
	public Categoria save(Categoria obj) {
		
		obj.setId(null);
		
		return categoriaRepository.save(obj);
	}
	
	
	public Categoria update(Categoria obj) {
		
		return categoriaRepository.save(obj);
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
}
