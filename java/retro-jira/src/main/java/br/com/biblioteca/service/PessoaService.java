package br.com.biblioteca.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.biblioteca.dto.request.PessoaRequestDTO;
import br.com.biblioteca.dto.response.PessoaResponseDTO;
import br.com.biblioteca.model.PessoaModel;
import br.com.biblioteca.repository.PessoaRepository;
import br.com.biblioteca.utils.ObjectMapperUtil;
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

        ObjectMapper objectMapper = ObjectMapperUtil.getInstance();

        return objectMapper.convertValue(pessoa, PessoaResponseDTO.class);
    }

    public List<PessoaResponseDTO> buscarGerentes() {
        List<PessoaModel> gerenteLista = pessoaRepository.findByGerenteTrue();
        return buscarPessoas(gerenteLista);
    }

    public List<PessoaResponseDTO> buscarFuncionarios() {
        List<PessoaModel> funcionarioLista = pessoaRepository.findByFuncionarioTrue();
        return buscarPessoas(funcionarioLista);
    }

    private List<PessoaResponseDTO> buscarPessoas(List<PessoaModel> pessoaLista) {

        ObjectMapper objectMapper = ObjectMapperUtil.getInstance();

        return pessoaLista.stream()
                .map(model -> objectMapper.convertValue(model, PessoaResponseDTO.class))
                .collect(Collectors.toList());
    }

    protected PessoaModel buscarGerenteById(Long gerenteId) {
        return pessoaRepository.findByGerenteTrueById(gerenteId).orElseThrow(() -> new IllegalArgumentException(
                "Gerente não encontrado com o ID: " + gerenteId));
    }

    protected List<PessoaModel> buscarMembrosByIdList(List<Long> membrosIds) {
        return pessoaRepository.findByFuncionarioTrueByIds(membrosIds);
    }

}
