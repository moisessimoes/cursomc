package com.moises.cursomc.domain.enums;

public enum EstadoPagamento {
	
	PENDENTE(1, "Pendente"),
	QUITADO(2, "Quitado"),
	CANCELADO(3, "Cancelado");
	
	private int codigo;
	private String descricao;
	
	private EstadoPagamento(int cod, String desc) {
		
		this.codigo = cod;
		this.descricao = desc;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	
	public static EstadoPagamento toEnum(Integer codigo) {
		
		if(codigo == null) return null;
		
		for (EstadoPagamento ep : EstadoPagamento.values()) {
			
			if(codigo.equals(ep.getCodigo())) {
				
				return ep;
			}
		}
		
		throw new IllegalArgumentException("ID: " + codigo + " é INVÁLIDO!");
	}
}
