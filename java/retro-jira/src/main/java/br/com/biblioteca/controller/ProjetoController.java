package br.com.biblioteca.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.biblioteca.dto.request.ProjetoRequestDTO;
import br.com.biblioteca.dto.response.ProjetoResponseDTO;
import br.com.biblioteca.enums.RiscoEnum;
import br.com.biblioteca.enums.StatusEnum;
import br.com.biblioteca.service.ProjetoService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@Hidden
@SuppressWarnings("java:S6833")
@AllArgsConstructor
@Controller
@RequestMapping("/projetos")
public class ProjetoController {

	private static final String URL = "redirect:/projetos";

	private final ProjetoService projetoService;

	@GetMapping
	public String home(Model model, HttpServletRequest httpServletRequest) {
		model.addAttribute("projetos", projetoService.listarTodosProjetos());
		model.addAttribute("riscoOptions", RiscoEnum.values());
		model.addAttribute("statusOptions", StatusEnum.values());

		return "index";
	}

	@PostMapping("/salvar")
	public String salvarProjeto(@ModelAttribute ProjetoRequestDTO projeto,
			RedirectAttributes redirectAttributes) {
		try {
			projetoService.salvarProjeto(projeto);

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erroMensagem", e.getMessage());
		}
		return URL;
	}

	@PostMapping("/excluir/{id}")
	public String excluirProjeto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			projetoService.excluirProjeto(id);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erroMensagem", e.getMessage());
		}
		return URL;
	}

	@GetMapping("/buscar/{id}")
	@ResponseBody
	public ProjetoResponseDTO buscarProjetoPorId(@PathVariable Long id) {
		return projetoService.buscarProjetoPorId(id);
	}

	@GetMapping("/pesquisar")
	public String pesquisarProjetos(@RequestParam String nome, Model model) {
		List<ProjetoResponseDTO> projetos;

		if (nome == null || nome.trim().isEmpty())
			projetos = projetoService.listarTodosProjetos();
		else
			projetos = projetoService.buscarProjetosPorNome(nome);

		model.addAttribute("projetos", projetos);
		return "fragments/projetos";
	}

}
