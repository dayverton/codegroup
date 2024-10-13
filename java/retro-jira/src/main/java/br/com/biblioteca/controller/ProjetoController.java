package br.com.biblioteca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/projetos")
public class ProjetoController {

	@GetMapping
	public String home(Model model, HttpServletRequest httpServletRequest) {
		model.addAttribute("projetos", "projetos");
		return "index";
	}
}
