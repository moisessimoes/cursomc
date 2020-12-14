package com.moises.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.moises.cursomc.domain.Cidade;
import com.moises.cursomc.domain.Cliente;
import com.moises.cursomc.domain.Endereco;
import com.moises.cursomc.domain.enums.Perfil;
import com.moises.cursomc.domain.enums.TipoCliente;
import com.moises.cursomc.dto.ClienteDTO;
import com.moises.cursomc.dto.NovoClienteDTO;
import com.moises.cursomc.repositories.ClienteRepository;
import com.moises.cursomc.repositories.EnderecoRepository;
import com.moises.cursomc.security.UserSpringSecurity;
import com.moises.cursomc.services.exceptions.AuthorizationException;
import com.moises.cursomc.services.exceptions.DataIntegrityException;
import com.moises.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private BCryptPasswordEncoder bcpe;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImageService imageService;
	
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	@Value("${img.profile.size}")
	private Integer size;
	
	//===========================================================================================================================
	
	public Cliente find(Integer id) {
		
		UserSpringSecurity user = UserService.authenticated();
		
		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) throw new AuthorizationException("ACESSO NEGADO!");
		
		Optional<Cliente> obj = clienteRepository.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! ID: " + id + " Tipo: " + Cliente.class.getName()));
	}
	
	//===========================================================================================================================
	
	@Transactional
	public Cliente save(Cliente obj) {
		
		obj.setId(null);
		
		obj = clienteRepository.save(obj);
		
		enderecoRepository.saveAll(obj.getEnderecos());
		
		return obj;
	}
	
	//===========================================================================================================================
	
	public Cliente update(Cliente obj) {
		
		Cliente newObj = find(obj.getId());
		
		updateData(newObj, obj);
		
		return clienteRepository.save(newObj);
	}
	
	//===========================================================================================================================
	
	private void updateData(Cliente newObj, Cliente obj) {
		
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
	//===========================================================================================================================

	public void delete(Integer id) {
		
		try {
			clienteRepository.deleteById(id);
			
		} catch (DataIntegrityViolationException dive) {
			throw new DataIntegrityException("Não é possível excluir porque há pedidos relacionados com este cliente.");
		}
	}
	
	//===========================================================================================================================
	
	public List<Cliente> findAll() {
		
		return clienteRepository.findAll();
	}
	
	//===========================================================================================================================
	
	public Cliente findByEmail(String email) {
		
		UserSpringSecurity user = UserService.authenticated();
		
		if(user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) throw new AuthorizationException("ACESSO NEGADO!");
		
		Cliente obj = clienteRepository.findByEmail(email);
		
		if(obj == null) throw new ObjectNotFoundException("Objeto não encontrado! ID: " + user.getId() + " Tipo: " + Cliente.class.getName());
		
		return obj;
	}
	
	//===========================================================================================================================
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		
		PageRequest pgR = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return clienteRepository.findAll(pgR);
	}
	
	//===========================================================================================================================
	
	public Cliente fromDTO(ClienteDTO objDTO) {
		
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null, null);
	}
	
	//===========================================================================================================================
	
	public Cliente fromDTO(NovoClienteDTO objDTO) {
		
		Cliente cli = new Cliente(null, objDTO.getNome(), objDTO.getEmail(), objDTO.getCpfOuCnpj(), TipoCliente.toEnum(objDTO.getTipo()), bcpe.encode(objDTO.getSenha()));
		
		Cidade city = new Cidade(objDTO.getCidadeID(), null, null);
		
		Endereco end = new Endereco(null, objDTO.getLogradouro(), objDTO.getNumero(), objDTO.getComplemento(), objDTO.getBairro(), objDTO.getCep(), cli, city);
		
		cli.getEnderecos().add(end);
		
		cli.getTelefones().add(objDTO.getTelefone1());
		
		if(objDTO.getTelefone2() != null) cli.getTelefones().add(objDTO.getTelefone2()); //Se o telefone2 nao for null, ele adiciona.
		
		if(objDTO.getTelefone3() != null) cli.getTelefones().add(objDTO.getTelefone3()); //Se o telefone3 nao for null, ele adiciona
		
		return cli; 
	}
	
	//===========================================================================================================================
	
	public URI uploadProfilePicture(MultipartFile mpf) { //METODO QUE FAZ O UPLOAD DE UMA IMAGEM DE PERFIL PARA O AMAZON S3
		
		UserSpringSecurity user = UserService.authenticated();
		
		if(user == null) throw new AuthorizationException("ACESSO NEGADO!");
		
		BufferedImage jpgImage = imageService.getJpgImageFromFile(mpf);
		
		jpgImage = imageService.cropSquare(jpgImage); //RECORTANDO A IMAGEM PARA DEIXAR ELA QUADRADA
		jpgImage = imageService.resize(jpgImage, size); //REDIMENSIOANDO A IMAGEM
		
		String fileName = prefix + user.getId() + ".jpg";
		
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
	}
}
