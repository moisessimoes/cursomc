package com.moises.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moises.cursomc.domain.Cliente;
import com.moises.cursomc.domain.ItemPedido;
import com.moises.cursomc.domain.PagamentoComBoleto;
import com.moises.cursomc.domain.Pedido;
import com.moises.cursomc.domain.enums.EstadoPagamento;
import com.moises.cursomc.repositories.ItemPedidoRepository;
import com.moises.cursomc.repositories.PagamentoRepository;
import com.moises.cursomc.repositories.PedidoRepository;
import com.moises.cursomc.security.UserSpringSecurity;
import com.moises.cursomc.services.exceptions.AuthorizationException;
import com.moises.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private EmailService gmailService; //Se colocar o nome dessa variavel de emailService, o programa envia o email em forma de texto apenas.
	
	//===========================================================================================================================
	
	public Pedido find(Integer id) {
		
		Optional<Pedido> obj = pedidoRepository.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! ID: " + id + " Tipo: " + Pedido.class.getName()));
	}
	
	//===========================================================================================================================
	
	@Transactional
	public Pedido save(Pedido obj) {
		
		obj.setId(null);
		
		obj.setInstante(new Date());
		
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		
		obj.getPagamento().setPedido(obj);
		
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		
		obj = pedidoRepository.save(obj); //Salvando o Pedido
		
		pagamentoRepository.save(obj.getPagamento()); //Salvando O Pagamento
		
		for (ItemPedido ip : obj.getItens()) {
			
			ip.setDesconto(0.0);
			
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			
			ip.setPreco(ip.getProduto().getPreco());
			
			ip.setPedido(obj);
		}
		
		itemPedidoRepository.saveAll(obj.getItens()); //Salvando so itens do pedido
		
		gmailService.sendOrderConfirmationHtmlEmail(obj);
		
		return obj;
	}
	
	
	//=========================================================================================================================== 
	
	//Restrição de conteúdo: cliente só recupera seus pedidos
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		
		UserSpringSecurity user = UserService.authenticated();
		
		if(user == null) throw new AuthorizationException("ACESSO NEGADO!");
		
		PageRequest pgR = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		Cliente cliente = clienteService.find(user.getId());
		
		return pedidoRepository.findByCliente(cliente, pgR);
	}
}
