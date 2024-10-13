package br.com.biblioteca.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.biblioteca.dto.request.PessoaRequestDTO;
import br.com.biblioteca.dto.response.PessoaResponseDTO;
import br.com.biblioteca.model.PessoaModel;
import br.com.biblioteca.repository.PessoaRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    public PessoaResponseDTO cadastrarPessoa(PessoaRequestDTO pessoaDTO) {
        PessoaModel pessoa = PessoaModel.builder()
                .nome(pessoaDTO.nome())
                .cpf(pessoaDTO.cpf())
                .dataNascimento(pessoaDTO.dataNascimento())
                .funcionario(pessoaDTO.atribuicao().equalsIgnoreCase("funcionario"))
                .gerente(pessoaDTO.atribuicao().equalsIgnoreCase("gerente"))
                .build();

        pessoaRepository.saveAndFlush(pessoa);

        ObjectMapper objectMapper = instanciarMapeador();

        return objectMapper.convertValue(pessoa, PessoaResponseDTO.class);
    }

    public List<PessoaResponseDTO> buscarPessoas() {
        List<PessoaModel> pessoaLista = pessoaRepository.findAllByOrderByIdDesc();

        ObjectMapper objectMapper = instanciarMapeador();

        return pessoaLista.stream()
                .map(model -> objectMapper.convertValue(model, PessoaResponseDTO.class))
                .collect(Collectors.toList());
    }

    private ObjectMapper instanciarMapeador() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper;
    }

    public List<PessoaResponseDTO> buscarGerentes() {
        List<PessoaModel> gerenteLista = pessoaRepository.findByGerenteTrue();

        ObjectMapper objectMapper = instanciarMapeador();

        return gerenteLista.stream()
                .map(model -> objectMapper.convertValue(model, PessoaResponseDTO.class))
                .collect(Collectors.toList());
    }

    public List<PessoaResponseDTO> buscarFuncionarios() {
        List<PessoaModel> funcionarioLista = pessoaRepository.findByFuncionarioTrue();

        ObjectMapper objectMapper = instanciarMapeador();

        return funcionarioLista.stream()
                .map(model -> objectMapper.convertValue(model, PessoaResponseDTO.class))
                .collect(Collectors.toList());
    }

}
