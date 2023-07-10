package Spring6.Spring6.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

@Entity
public class Role implements GrantedAuthority{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String nomeRole;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return nomeRole;
	}
	public void setName(String name) {
		this.nomeRole = name;
	}
	@Override
	public String getAuthority() {  // ROLE_ADMIN, ROLE_GERENTE, ROLE_SECRETARIO
		// TODO Auto-generated method stub
		return this.nomeRole;
	}
	
}
