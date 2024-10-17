package br.com.biblioteca.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.biblioteca.dto.request.ProjetoRequestDTO;
import br.com.biblioteca.dto.response.GerenteDTO;
import br.com.biblioteca.dto.response.ProjetoResponseDTO;
import br.com.biblioteca.enums.RiscoEnum;
import br.com.biblioteca.enums.StatusEnum;
import br.com.biblioteca.model.PessoaModel;
import br.com.biblioteca.model.ProjetoModel;
import br.com.biblioteca.repository.ProjetoRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final PessoaService pessoaService;

    private static final String PROJETO_NAO_ENCONTRADO = "Projeto não encontrado";
    private static final String PROJETO_EXCLUSAO_NAO_PERMITIDA = "Não é possível excluir um projeto em andamento, iniciado ou encerrado";

    public List<ProjetoResponseDTO> listarTodosProjetos() {
        List<ProjetoModel> projetoLista = projetoRepository.findAllByOrderByIdAsc();

        return projetoLista.stream()
                .map(this::converterProjetoResponse)
                .collect(Collectors.toList());
    }

    public void salvarProjeto(ProjetoRequestDTO projetoDTO) {
        PessoaModel gerente = pessoaService.buscarGerenteById(projetoDTO.gerente());
        List<PessoaModel> membros = pessoaService.buscarMembrosByIdList(projetoDTO.membros());

        ProjetoModel projeto = ProjetoModel.builder()
                .id(projetoDTO.id())
                .nome(projetoDTO.nome())
                .dataInicio(projetoDTO.dataInicio())
                .dataPrevisaoFim(projetoDTO.dataPrevisaoFim())
                .dataFim(projetoDTO.dataFim())
                .descricao(projetoDTO.descricao())
                .orcamento(projetoDTO.orcamento())
                .risco(RiscoEnum.valueOf(projetoDTO.risco()))
                .status(StatusEnum.valueOf(projetoDTO.status()))
                .gerente(gerente)
                .membros(membros)
                .build();

        projetoRepository.save(projeto);
    }

    public void excluirProjeto(Long id) {
        ProjetoModel projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(PROJETO_NAO_ENCONTRADO));

        if (projeto.getStatus() == StatusEnum.INICIADO || projeto.getStatus() == StatusEnum.EM_ANDAMENTO
                || projeto.getStatus() == StatusEnum.ENCERRADO) {
            throw new IllegalStateException(PROJETO_EXCLUSAO_NAO_PERMITIDA);
        }
        projetoRepository.delete(projeto);
    }

    public ProjetoResponseDTO buscarProjetoPorId(Long id) {
        ProjetoModel projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(PROJETO_NAO_ENCONTRADO));

        return converterProjetoResponse(projeto);

    }

    public List<ProjetoResponseDTO> buscarProjetosPorNome(String nome) {
        List<ProjetoModel> projetoLista = projetoRepository.findByNomeContainingIgnoreCase(nome);

        return projetoLista.stream()
                .map(this::converterProjetoResponse)
                .collect(Collectors.toList());
    }

    private ProjetoResponseDTO converterProjetoResponse(ProjetoModel projeto) {
        return ProjetoResponseDTO.builder()
                .nome(projeto.getNome())
                .id(projeto.getId())
                .gerente(GerenteDTO.builder().nome(projeto.getGerente().getNome())
                        .id(projeto.getGerente().getId())
                        .build())
                .dataInicio(projeto.getDataInicio())
                .status(projeto.getStatus().name())
                .risco(projeto.getRisco().name())
                .dataPrevisaoFim(projeto.getDataPrevisaoFim())
                .dataFim(projeto.getDataFim())
                .orcamento(projeto.getOrcamento())
                .descricao(projeto.getDescricao())
                .membros(projeto.getMembros().stream()
                        .map(x -> x.getId())
                        .collect(Collectors.toList()))
                .build();
    }

}
