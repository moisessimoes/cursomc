package com.moises.cursomc.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.moises.cursomc.domain.PagamentoComBoleto;

@Service
public class BoletoService {
	
	/*Esse metodo acrescenta 7 dias na data de vencimento com base na data do pedido.
	 * 
	 * Em uma implementação real, esse código abaixo seria substituido por um WebService
	 * 
	 * que iria gerar um boleto de pagamento já com uma data de vencimento.
	 * 
	 * */
	
	public void preencherPagamentoComBoleto(PagamentoComBoleto pagto, Date instanteDoPedido) {
		
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(instanteDoPedido);
		
		cal.add(Calendar.DAY_OF_MONTH, 7);
		
		pagto.setDataVencimento(cal.getTime());
	}
}
