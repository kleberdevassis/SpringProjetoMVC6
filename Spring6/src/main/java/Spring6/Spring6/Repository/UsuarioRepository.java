package Spring6.Spring6.Repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import Spring6.Spring6.Model.Usuario;

@Repository
@Transactional
public interface UsuarioRepository extends CrudRepository<Usuario, Long>{
	
	
	@Query("select u from Usuario u where u.login= ?1")
	Usuario finUserByLogin(String login);

}
