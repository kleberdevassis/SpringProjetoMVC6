package Spring6.Spring6.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import Spring6.Spring6.Model.Pessoa;

@Repository
public interface PessoaRepository extends CrudRepository<Pessoa, Long> {
	
	@Query("SELECT p FROM Pessoa p WHERE p.nome LIKE %:nome%")
	List<Pessoa> buscaPessoaPorNome(@Param("nome") String nome);

}
