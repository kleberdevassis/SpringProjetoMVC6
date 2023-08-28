package Spring6.Spring6.Repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	default Page<Pessoa> findPessoaByNamePage(String nome, Pageable pageable){
		Pessoa pessoa = new Pessoa();
		pessoa.setNome(nome);
		
		/*valor e configuração de consulta*/
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		/*objeto com o valor e conf para consulta*/
		Example<Pessoa> example = Example.of(pessoa, exampleMatcher);
		
		Page<Pessoa> pessoas = findAll(example, pageable);
		
		return pessoas;
	}
	
	default Page<Pessoa> findPessoaBySexoPage(String nome,String sexo, Pageable pageable){
		Pessoa pessoa = new Pessoa();
		pessoa.setNome(nome);
		pessoa.setSexopessoa(sexo);
		
		/*valor e configuração de consulta*/
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher("sexopessoa", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		/*objeto com o valor e conf para consulta*/
		Example<Pessoa> example = Example.of(pessoa, exampleMatcher);
		
		Page<Pessoa> pessoas = findAll(example, pageable);
		
		return pessoas;
	}

}
