package Spring6.Spring6.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import Spring6.Spring6.Model.Pessoa;
import Spring6.Spring6.Model.Telefone;
import Spring6.Spring6.Repository.PessoaRepository;
import Spring6.Spring6.Repository.ProfissaoRepository;
import Spring6.Spring6.Repository.TelefoneRepository;

@Controller
public class PessoaController {
	
	@Autowired
	PessoaRepository pessoaRepository;
	
	@Autowired
	TelefoneRepository telefoneRepository;

	@Autowired
	ReportUtil reportUtil;
	
	@Autowired
	ProfissaoRepository profissaoRepository;
	
	@GetMapping(value = "**/cadastro")
	public ModelAndView inicio() {
		ModelAndView andView = new ModelAndView("cadastros/cadastro");
		andView.addObject("pessoaobj", new Pessoa());
		andView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5,Sort.by("nome"))));
		andView.addObject("profissoes", profissaoRepository.findAll());
		return andView;
		
	}
	
	@GetMapping("/pessoaspag")
	public ModelAndView carregarPessoaPorPaginacao(@PageableDefault(size=5)Pageable pageable, ModelAndView model) {
		
		Page<Pessoa> pagePessoa = pessoaRepository.findAll(pageable);
		model.addObject("pessoas", pagePessoa);
		model.addObject("pessoaobj", new Pessoa());
		model.setViewName("cadastros/cadastro");
		
		return model;
	}
	
	@PostMapping(value = "**/salvarpessoa", consumes= {"multipart/form-data"})
	public ModelAndView salvarPessoa(Pessoa pessoa, final MultipartFile file) throws IOException {
		
		pessoa.setTelefones(telefoneRepository.getTelefones(pessoa.getId()));
		
		
		if(file.getSize() > 0) {
			pessoa.setCurriculo(file.getBytes());
			pessoa.setTipoFileCurriculo(file.getContentType());
			pessoa.setNomeFileCurriculo(file.getOriginalFilename());
		}else {
			if(pessoa.getId() !=null && pessoa.getId() > 0) {
				Pessoa pessoalTemp = pessoaRepository.findById(pessoa.getId()).get();
				pessoa.setCurriculo(pessoalTemp.getCurriculo());
				pessoa.setTipoFileCurriculo(pessoalTemp.getTipoFileCurriculo());
				pessoa.setNomeFileCurriculo(pessoalTemp.getNomeFileCurriculo());
			}
		}
		
		pessoaRepository.save(pessoa);
		ModelAndView andView = new ModelAndView("cadastros/cadastro");
		andView.addObject("pessoaobj", new Pessoa());
		andView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5,Sort.by("nome"))));
		andView.addObject("profissoes", profissaoRepository.findAll());
		return andView;
	}
	
	@GetMapping(value="**/listapessoas")
	public ModelAndView pessoas() {
		ModelAndView andView = new ModelAndView("cadastros/cadastro");
		andView.addObject("pessoaobj", new Pessoa());
		andView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5,Sort.by("nome"))));
		return andView;
	}
	
	
	@GetMapping(value="/editarpessoa/{idpessoa}")
	public ModelAndView editar(@PathVariable("idpessoa")Long idpessoa) {
		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		ModelAndView andView = new ModelAndView("cadastros/cadastro");
		andView.addObject("pessoaobj", pessoa.get());
		andView.addObject("profissoes", profissaoRepository.findAll());
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
	
	@GetMapping("/baixarcurriculo/{idpessoa}")
	public void baixarcurriculo(@PathVariable("idpessoa")Long idpessoa, HttpServletResponse response) throws IOException {
		
		Pessoa pessoa = pessoaRepository.findById(idpessoa).get();
		if(pessoa.getCurriculo() != null) {
			
			/*tamanho da resposta*/
			response.setContentLength(pessoa.getCurriculo().length);
			
			/*pode ser generico  ->   application/octet-stream*/
			response.setContentType(pessoa.getTipoFileCurriculo());
			
			/*define o cabeçalho.*/
			String headerkey = "Content-Diposition";
			String headerValue = String.format("attachment; filename=\"%s\"", pessoa.getNomeFileCurriculo());
			response.setHeader(headerkey, headerValue);
			
			response.getOutputStream().write(pessoa.getCurriculo());
		}
		
		
	}
	

	@GetMapping(value="**/buscapornome")
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
		andView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5,Sort.by("nome"))));
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