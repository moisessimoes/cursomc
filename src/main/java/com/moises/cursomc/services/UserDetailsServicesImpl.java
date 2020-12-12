package com.moises.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.moises.cursomc.domain.Cliente;
import com.moises.cursomc.repositories.ClienteRepository;
import com.moises.cursomc.security.UserSpringSecurity;

@Service
public class UserDetailsServicesImpl implements UserDetailsService {

	@Autowired
	private ClienteRepository cr;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Cliente cli = cr.findByEmail(email);
		
		if(cli == null) throw new UsernameNotFoundException(email);
		
		return new UserSpringSecurity(cli.getId(), cli.getEmail(), cli.getSenha(), cli.getPerfis());
	}
}
