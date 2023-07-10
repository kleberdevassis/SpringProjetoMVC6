package Spring6.Spring6.Security;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import Spring6.Spring6.Model.Usuario;
import Spring6.Spring6.Repository.UsuarioRepository;

@Service
@Transactional
public class ImplementacaoUserDetailsService implements UserDetailsService{
	
	@Autowired
	UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    
		
		Usuario usuario = usuarioRepository.finUserByLogin(username);
		
		if(usuario == null) {
			
			throw new UsernameNotFoundException("usuario não foi encontrado");
				
		}
		
		return  new org.springframework.security.core.userdetails.User(usuario.getLogin(), usuario.getPassword(), usuario.isEnabled(), true, true, true, usuario.getAuthorities());
		
	}

}
