package br.com.biblioteca.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.biblioteca.dto.request.ProjetoRequestDTO;
import br.com.biblioteca.dto.response.ProjetoResponseDTO;
import br.com.biblioteca.enums.RiscoEnum;
import br.com.biblioteca.enums.StatusEnum;
import br.com.biblioteca.model.PessoaModel;
import br.com.biblioteca.model.ProjetoModel;
import br.com.biblioteca.repository.ProjetoRepository;

@ExtendWith(MockitoExtension.class)
class ProjetoServiceTest {

    @Mock
    private ProjetoRepository projetoRepository;

    @Mock
    private PessoaService pessoaService;

    @InjectMocks
    private ProjetoService projetoService;

    private static final String PROJETO_NAO_ENCONTRADO = "Projeto não encontrado";
    private static final String PROJETO_EXCLUSAO_NAO_PERMITIDA = "Não é possível excluir um projeto em andamento, iniciado ou encerrado";

    @Test
    void testListarTodosProjetos() {
        ProjetoModel projeto1 = ProjetoModel.builder()
                .id(1L)
                .nome("Projeto A")
                .gerente(new PessoaModel())
                .status(StatusEnum.EM_ANALISE)
                .risco(RiscoEnum.ALTO_RISCO)
                .membros(new ArrayList<PessoaModel>())
                .build();

        ProjetoModel projeto2 = ProjetoModel.builder()
                .id(2L)
                .nome("Projeto B")
                .gerente(new PessoaModel())
                .status(StatusEnum.EM_ANALISE)
                .risco(RiscoEnum.ALTO_RISCO)
                .membros(new ArrayList<PessoaModel>())
                .build();

        List<ProjetoModel> projetoLista = Arrays.asList(projeto1, projeto2);

        when(projetoRepository.findAllByOrderByIdAsc()).thenReturn(projetoLista);

        List<ProjetoResponseDTO> resultado = projetoService.listarTodosProjetos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Projeto A", resultado.get(0).getNome());
        verify(projetoRepository).findAllByOrderByIdAsc();
    }

    @Test
    void testSalvarProjeto() {
        ProjetoRequestDTO projetoDTO = new ProjetoRequestDTO(
                null,
                "Novo Projeto",
                LocalDate.now(),
                1L,
                StatusEnum.EM_ANALISE.name(),
                RiscoEnum.ALTO_RISCO.name(),
                LocalDate.now().plusDays(30),
                null,
                2000.0f,
                "Descrição",
                Collections.emptyList());

        PessoaModel gerente = new PessoaModel();

        when(pessoaService.buscarGerenteById(projetoDTO.gerente())).thenReturn(gerente);

        projetoService.salvarProjeto(projetoDTO);

        verify(projetoRepository).save(any(ProjetoModel.class));
    }

    @Test
    void testExcluirProjeto_ProjetoIniciado() {
        Long id = 1L;
        ProjetoModel projeto = ProjetoModel.builder().id(id).status(StatusEnum.INICIADO).build();

        when(projetoRepository.findById(id)).thenReturn(Optional.of(projeto));

        Exception exception = assertThrows(IllegalStateException.class, () -> projetoService.excluirProjeto(id));
        assertEquals(PROJETO_EXCLUSAO_NAO_PERMITIDA, exception.getMessage());
    }

    @Test
    void testBuscarProjetoPorId_NotFound() {
        Long id = 1L;
        when(projetoRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> projetoService.buscarProjetoPorId(id));
        assertEquals(PROJETO_NAO_ENCONTRADO, exception.getMessage());
    }

    @Test
    void testExcluirProjeto_Success() {
        Long id = 1L;
        ProjetoModel projeto = ProjetoModel.builder().id(id).status(StatusEnum.PLANEJADO).build();

        when(projetoRepository.findById(id)).thenReturn(Optional.of(projeto));

        projetoService.excluirProjeto(id);

        verify(projetoRepository).delete(projeto);
    }

    @Test
    void testBuscarProjetosPorNome() {
        String nome = "Projeto Teste";
        ProjetoModel projeto = ProjetoModel.builder()
                .id(1L)
                .nome(nome)
                .gerente(new PessoaModel())
                .status(StatusEnum.ANALISE_APROVADA)
                .risco(RiscoEnum.ALTO_RISCO)
                .membros(Arrays.asList(new PessoaModel()))
                .build();

        List<ProjetoModel> projetoLista = Collections.singletonList(projeto);

        when(projetoRepository.findByNomeContainingIgnoreCase(nome)).thenReturn(projetoLista);

        List<ProjetoResponseDTO> resultado = projetoService.buscarProjetosPorNome(nome);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(nome, resultado.get(0).getNome());
        verify(projetoRepository).findByNomeContainingIgnoreCase(nome);
    }

    @Test
    void testBuscarProjetoPorId_Success() {
        Long id = 1L;
        ProjetoModel projeto = ProjetoModel.builder()
                .id(id)
                .nome("Projeto Teste")
                .gerente(new PessoaModel())
                .status(StatusEnum.ANALISE_APROVADA)
                .risco(RiscoEnum.ALTO_RISCO)
                .membros(Arrays.asList(new PessoaModel()))
                .build();

        when(projetoRepository.findById(id)).thenReturn(Optional.of(projeto));

        ProjetoResponseDTO resultado = projetoService.buscarProjetoPorId(id);

        assertNotNull(resultado);
        assertEquals(projeto.getNome(), resultado.getNome());
        verify(projetoRepository).findById(id);
    }

}
