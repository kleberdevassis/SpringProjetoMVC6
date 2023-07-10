package Spring6.Spring6.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter{
	
  @Autowired
  ImplementacaoUserDetailsService implementacaoUserDetailsService;

	
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
      http.csrf().disable()
          .authorizeRequests()
              .antMatchers(HttpMethod.GET, "/").permitAll()
              .antMatchers(HttpMethod.GET, "/cadastro").hasRole("ADMIN")
              .anyRequest().authenticated()
          .and().formLogin().loginPage("/login").permitAll()
              .defaultSuccessUrl("/cadastro")
              .failureUrl("/login?error=true")
          .and().logout().logoutSuccessUrl("/login")
              .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
  }

	
	@Override 
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(implementacaoUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
		
		/*auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
		.withUser("kleber").password("$2a$10$Q7B.d3SSqJNuFmz.uxZxJOOm/1Yx.uegeKiz873tbcM0zsnJ5lxxm").roles("ADMIN");*/
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/static/**");
	}


}
