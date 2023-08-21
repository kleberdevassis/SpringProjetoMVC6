package Spring6.Spring6.Repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import Spring6.Spring6.Model.Pessoa;

@Repository
@Transactional
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
	
	@Query("SELECT p FROM Pessoa p WHERE p.nome LIKE %:nome%")
	List<Pessoa> buscaPessoaPorNome(@Param("nome") String nome);
	
	@Query("SELECT p FROM Pessoa p WHERE p.sexopessoa = ?1 ")
	List<Pessoa> buscaPessoaPorSexo(@Param("sexo") String sexo);

	@Query("SELECT p FROM Pessoa p WHERE p.nome LIKE %:nome% AND p.sexopessoa = :sexopessoa")
	List<Pessoa> buscaPessoaPorSexo(@Param("nome") String nome, @Param("sexopessoa") String sexopessoa);

}
