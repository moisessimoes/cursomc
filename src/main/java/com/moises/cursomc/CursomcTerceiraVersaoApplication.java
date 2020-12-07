package com.moises.cursomc;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.moises.cursomc.domain.Categoria;
import com.moises.cursomc.domain.Cidade;
import com.moises.cursomc.domain.Cliente;
import com.moises.cursomc.domain.Endereco;
import com.moises.cursomc.domain.Estado;
import com.moises.cursomc.domain.Pagamento;
import com.moises.cursomc.domain.PagamentoComBoleto;
import com.moises.cursomc.domain.PagamentoComCartao;
import com.moises.cursomc.domain.Pedido;
import com.moises.cursomc.domain.Produto;
import com.moises.cursomc.domain.enums.EstadoPagamento;
import com.moises.cursomc.domain.enums.TipoCliente;
import com.moises.cursomc.repositories.CategoriaRepository;
import com.moises.cursomc.repositories.CidadeRepository;
import com.moises.cursomc.repositories.ClienteRepository;
import com.moises.cursomc.repositories.EnderecoRepository;
import com.moises.cursomc.repositories.EstadoRepository;
import com.moises.cursomc.repositories.PagamentoRepository;
import com.moises.cursomc.repositories.PedidoRepository;
import com.moises.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcTerceiraVersaoApplication implements CommandLineRunner {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;

	
	public static void main(String[] args) {
		SpringApplication.run(CursomcTerceiraVersaoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Categoria cat1 = new Categoria(null, "Informárica");
		Categoria cat2 = new Categoria(null, "Escritório");
		
		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);
		
		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));
		
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));
		
		categoriaRepository.saveAll(Arrays.asList(cat1, cat2));
		
		produtoRepository.saveAll(Arrays.asList(p1, p2, p3));
		
		
		//--------------------------------------------------------------------------------------------------------------------
		
		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");
		
		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);
		
		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2, c3));
		
		estadoRepository.saveAll(Arrays.asList(est1, est2));
		
		cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));
		
		
		//--------------------------------------------------------------------------------------------------------------------
		
		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "36378912377", TipoCliente.PESSOAFISICA);
		
		cli1.getTelefones().addAll(Arrays.asList("27363323", "93838393"));
		
		Endereco ed1 = new Endereco(null, "Rua Flores", "300", "Apto 303", "Jardim", "38220834", cli1, c1);
		Endereco ed2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "38777012", cli1, c2);
		
		cli1.getEnderecos().addAll(Arrays.asList(ed1, ed2));
		
		clienteRepository.saveAll(Arrays.asList(cli1));
		
		enderecoRepository.saveAll(Arrays.asList(ed1, ed2));
		
		//--------------------------------------------------------------------------------------------------------------------
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		Pedido ped1 = new Pedido(null, sdf.parse("07/12/2020 18:21"), cli1, ed1);
		Pedido ped2 = new Pedido(null, sdf.parse("08/12/2020 19:25"), cli1, ed2);
		
		Pagamento pgto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pgto1);
		
		Pagamento pgto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("10/12/2020 00:00"), null);
		ped2.setPagamento(pgto2);
 		
		
		cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));
		
		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		
		pagamentoRepository.saveAll(Arrays.asList(pgto1, pgto2));
	}
}
