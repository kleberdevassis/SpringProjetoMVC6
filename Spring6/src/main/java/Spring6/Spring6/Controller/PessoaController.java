package Spring6.Spring6.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import Spring6.Spring6.Model.Pessoa;
import Spring6.Spring6.Model.Telefone;
import Spring6.Spring6.Repository.PessoaRepository;
import Spring6.Spring6.Repository.TelefoneRepository;

@Controller
public class PessoaController {
	
	@Autowired
	PessoaRepository pessoaRepository;
	
	@Autowired
	TelefoneRepository telefoneRepository;

	@Autowired
	ReportUtil reportUtil;
	
	@GetMapping(value = "**/cadastro")
	public ModelAndView inicio() {
		ModelAndView andView = new ModelAndView("cadastros/cadastro");
		andView.addObject("pessoaobj", new Pessoa());
		return andView;
		
	}
	@PostMapping(value = "**/salvarpessoa")
	public ModelAndView salvarPessoa(Pessoa pessoa) {
		pessoa.setTelefones(telefoneRepository.getTelefones(pessoa.getId()));
		pessoaRepository.save(pessoa);
		ModelAndView andView = new ModelAndView("cadastros/cadastro");
		Iterable<Pessoa> pessoasit = pessoaRepository.findAll();
		andView.addObject("pessoaobj", new Pessoa());
		andView.addObject("pessoas", pessoasit);
		
		return andView;
	}
	
	@GetMapping(value="**/listapessoas")
	public ModelAndView pessoas() {
		ModelAndView andView = new ModelAndView("cadastros/cadastro");
		Iterable<Pessoa> pessoas = pessoaRepository.findAll();
		andView.addObject("pessoaobj", new Pessoa());
		andView.addObject("pessoas", pessoas);
		return andView;
	}
	
	
	@GetMapping(value="/editarpessoa/{idpessoa}")
	public ModelAndView editar(@PathVariable("idpessoa")Long idpessoa) {
		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		ModelAndView andView = new ModelAndView("cadastros/cadastro");
		andView.addObject("pessoaobj", pessoa.get());
		return andView;
	}
	
	@PostMapping(value="**/buscapornome")
	public ModelAndView buscaPorNome(@RequestParam("nomevalor") String nomevalor, @RequestParam("pesqsexo")String pesqsexo) {
		
		List<Pessoa> pessoas = new ArrayList<>();
		
		if(pesqsexo!=null && !pesqsexo.isEmpty()) {
			pessoas = pessoaRepository.buscaPessoaPorSexo(nomevalor, pesqsexo);
		}else {
			pessoas = pessoaRepository.buscaPessoaPorNome(nomevalor);
		}
		
	    ModelAndView andView = new ModelAndView("cadastros/cadastro");
	    andView.addObject("pessoas", pessoas);
	    andView.addObject("pessoaobj", new Pessoa());
	    return andView;
	}

	@GetMapping(value="**/buscandonome")
	public void imprimePdf(@RequestParam("nomevalor") String nomevalor,
			@RequestParam("pesqsexo")String pesqsexo,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		List<Pessoa> pessoas = new ArrayList<Pessoa>();
		if(pesqsexo != null && !pesqsexo.isEmpty()&& nomevalor != null && !nomevalor.isEmpty())  {
			pessoas = pessoaRepository.buscaPessoaPorSexo(nomevalor, pesqsexo);
		}else if(nomevalor != null && !nomevalor.isEmpty()) {
			pessoas = pessoaRepository.buscaPessoaPorNome(nomevalor);
			
		}else if(pesqsexo != null && !pesqsexo.isEmpty()) {
			pessoas = pessoaRepository.buscaPessoaPorSexo(pesqsexo);
			
		}else {
			Iterable<Pessoa> iterator = pessoaRepository.findAll();
			for(Pessoa pessoa : iterator) {
				pessoas.add(pessoa);
			}
		}
		
		byte[] pdf = reportUtil.gerarRelatorio(pessoas, "pessoa", request.getServletContext());
		
		response.setContentLength(pdf.length);
		
		response.setContentType("application/octet-stream");
		
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", "relatorio.pdf");
		response.setHeader(headerKey, headerValue);
		
		response.getOutputStream().write(pdf);
		
	}
	
	
	@GetMapping(value="/excluirpessoa/{idpessoa}")
	public ModelAndView excluir(@PathVariable("idpessoa")Long idpessoa) {
		pessoaRepository.deleteById(idpessoa);
		ModelAndView andView = new ModelAndView("cadastros/cadastro");
		andView.addObject("pessoas", pessoaRepository.findAll());
		andView.addObject("pessoaobj", new Pessoa());
		return andView;
	}
	
	@GetMapping(value="/telefones/{idpessoa}")
	public ModelAndView telefones(@PathVariable("idpessoa")Long idpessoa) {
		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		ModelAndView andView = new ModelAndView("cadastros/telefones");
		andView.addObject("pessoaobj", pessoa.get());
		andView.addObject("telefones", telefoneRepository.getTelefones(idpessoa));
		return andView;
	}

	@PostMapping(value="**/addtelefones/{idpessoa}")
	public ModelAndView Addtelefones(Telefone telefone, @PathVariable("idpessoa")Long idpessoa) {
		Pessoa pessoa = pessoaRepository.findById(idpessoa).get();
		
		if(telefone != null && telefone.getNumero().isEmpty() || telefone.getTipo().isEmpty()) {
			ModelAndView andView = new ModelAndView("cadastros/telefones");
			andView.addObject("telefones", telefoneRepository.getTelefones(idpessoa));
			andView.addObject("pessoaobj", pessoa);
			List<String> msg = new ArrayList<>();
			if(telefone.getNumero().isEmpty()) {
				msg.add("É NECESSÁRIO INSERIR O NÚMERO!");
			} if(telefone.getTipo().isEmpty()) {
				msg.add("É NECESSÁRIO INSERIR O TIPO!");{
				}
			}
			andView.addObject("msg", msg);
			return andView;
		}
		telefone.setPessoa(pessoa);
		telefoneRepository.save(telefone);
		ModelAndView andView = new ModelAndView("cadastros/telefones");
		andView.addObject("telefones", telefoneRepository.getTelefones(idpessoa));
		andView.addObject("pessoaobj", pessoa);
		return andView;
	}
	
	@GetMapping(value="**/removertelefone/{idtelefone}")
	public ModelAndView removertelefone(@PathVariable("idtelefone")Long idtelefone) {
		Pessoa pessoa = telefoneRepository.findById(idtelefone).get().getPessoa();
		
		 ModelAndView andView = new ModelAndView("cadastros/telefones");
		telefoneRepository.deleteById(idtelefone);
		
		andView.addObject("pessoaobj", pessoa);
		andView.addObject("telefones", telefoneRepository.getTelefones(pessoa.getId()));
		
		return andView;
	}
	
	
		
	
	
	}