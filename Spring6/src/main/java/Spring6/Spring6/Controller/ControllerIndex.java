package Spring6.Spring6.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerIndex {
	
	@GetMapping("**/")
	public String inicio() {
		return "index";
	}

}
