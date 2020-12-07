package com.moises.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moises.cursomc.domain.Categoria;
import com.moises.cursomc.repositories.CategoriaRepository;
import com.moises.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Categoria buscar(Integer id) {
		
		Optional<Categoria> obj = categoriaRepository.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! ID: " + id + " Tipo: " + Categoria.class.getName()));
	}
	
	
//	public void salvar() {
//		
//		Categoria cat1 = new Categoria(null, "Informárica");
//		Categoria cat2 = new Categoria(null, "Escritório");
//		
//		categoriaRepository.saveAll(Arrays.asList(cat1, cat2)); //Salvando no banco...
//	}
}
