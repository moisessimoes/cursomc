package com.moises.cursomc.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.moises.cursomc.domain.Categoria;
import com.moises.cursomc.domain.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
	
	@Transactional(readOnly = true)
	Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categorias, Pageable pgR);
	
	
	/*De inicio, o metodo de busca foi feito dessa forma:
	 * 
	 * 
	 * @Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias")
	   Page<Produto> search(@Param("nome") String nome, @Param("categorias") List<Categoria> categorias, Pageable pgR);
	   
	   Mas... Como Spring Data já possui nomes de consultas prontos para uso, optei por usá-lo.
	   
	   E o metodo atual ficou com essa nome acima: findDistinctByNomeContainingAndCategoriasIn
	 * 
	 * */

}
