package br.com.biblioteca.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.biblioteca.dto.response.PessoaResponseDTO;
import br.com.biblioteca.enums.RiscoEnum;
import br.com.biblioteca.enums.StatusEnum;
import br.com.biblioteca.model.ProjetoModel;
import br.com.biblioteca.service.PessoaService;
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
	private final PessoaService pessoaService;

	@GetMapping
	public String home(Model model, HttpServletRequest httpServletRequest) {
		model.addAttribute("projetos", projetoService.listarTodosProjetos());
		model.addAttribute("riscoOptions", RiscoEnum.values());
		model.addAttribute("statusOptions", StatusEnum.values());

		return "index";
	}

	@PostMapping("/salvar")
	public String salvarProjeto(@ModelAttribute ProjetoModel projeto,
			RedirectAttributes redirectAttributes) {
		try {
			projetoService.salvar(projeto);

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erroMensagem", e.getMessage());
		}
		return URL;
	}

	@GetMapping("/gerentes/buscar")
	@ResponseBody
	public List<PessoaResponseDTO> buscarGerentes() {
		return pessoaService.buscarGerentes();
	}

	@GetMapping("/funcionarios/buscar")
	@ResponseBody
	public List<PessoaResponseDTO> buscarFuncionarios() {
		return pessoaService.buscarFuncionarios();
	}

}
