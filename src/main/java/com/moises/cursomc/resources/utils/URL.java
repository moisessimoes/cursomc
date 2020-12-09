package com.moises.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {
	
	
	public static String decodeParam(String string) {
		
		try {
			
			return URLDecoder.decode(string, "UTF-8");
			
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	
	public static List<Integer> decodINtList(String string) {
		
		String[] vetor = string.split(",");
		
		List<Integer> lista = new ArrayList<>();
		
		for (int i = 0; i < vetor.length; i++) {
			
			lista.add(Integer.parseInt(vetor[i]));
		}
		
		return lista;
	}
	
	/*Outra forma de fazer esse metodo 'decodINtList' seria em apenas uma linha, que ficaria assim:
	 * 
	 * return Arrays.asList(string.split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList());
	 * 
	 * */
}
