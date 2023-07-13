package Spring6.Spring6.Controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerIndex {
	
	@GetMapping(value="**/")
	public String index() {
		return "index";
	}
	@GetMapping("/login")
	public String login() {
		return "login"; // <<< Retorna a página de login
	}
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return login(); // <<< Retorna a página de login
	}

}
