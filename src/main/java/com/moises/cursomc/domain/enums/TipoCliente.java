package com.moises.cursomc.domain.enums;

public enum TipoCliente {
	
	PESSOAFISICA(1, "Pessoa Física"),
	PESSOAJURIDICA(2, "Pessoa Jurídica");
	
	private int codigo;
	private String descricao;
	
	private TipoCliente(int cod, String desc) {
		
		this.codigo = cod;
		this.descricao = desc;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	
	public static TipoCliente toEnum(Integer codigo) {
		
		if(codigo == null) return null;
		
		for (TipoCliente tc : TipoCliente.values()) {
			
			if(codigo.equals(tc.getCodigo())) {
				
				return tc;
			}
		}
		
		throw new IllegalArgumentException("ID: " + codigo + " é INVÁLIDO!");
	}
}
