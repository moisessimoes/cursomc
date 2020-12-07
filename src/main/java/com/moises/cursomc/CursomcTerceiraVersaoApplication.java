package com.moises.cursomc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.moises.cursomc.services.CategoriaService;

@SpringBootApplication
public class CursomcTerceiraVersaoApplication implements CommandLineRunner {
	
	@Autowired
	private CategoriaService categoriaService;

	public static void main(String[] args) {
		SpringApplication.run(CursomcTerceiraVersaoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		categoriaService.salvar();
	}
}
