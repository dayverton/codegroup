package br.com.biblioteca.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.biblioteca.model.ProjetoModel;
import br.com.biblioteca.repository.ProjetoRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;

    public List<ProjetoModel> listarTodosProjetos() {
        return projetoRepository.findAllByOrderByIdAsc();
    }

    public void salvar(ProjetoModel projeto) {
        projetoRepository.save(projeto);
    }

}