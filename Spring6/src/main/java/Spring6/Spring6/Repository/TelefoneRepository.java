package Spring6.Spring6.Repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import Spring6.Spring6.Model.Telefone;

@Repository
@Transactional
public interface TelefoneRepository extends CrudRepository<Telefone, Long> {

    @Query("SELECT t FROM Telefone t WHERE t.pessoa.id = ?1")
    List<Telefone> getTelefones(Long pessoaId);
}

