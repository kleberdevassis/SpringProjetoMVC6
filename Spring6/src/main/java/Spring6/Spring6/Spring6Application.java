package Spring6.Spring6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootApplication
@EntityScan(basePackages = "Spring6.Spring6.Model")
@ComponentScan(basePackages = { "Spring6.Spring6", "Spring6.Spring6.Controller" })
@EnableJpaRepositories(basePackages = { "Spring6.Spring6.Repository" })
@EnableTransactionManagement
@EnableWebMvc
public class Spring6Application {

	public static void main(String[] args) {
		SpringApplication.run(Spring6Application.class, args);
		
		
		/*BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String result = encoder.encode("101112");
		System.out.println(result);*/
	}

}
