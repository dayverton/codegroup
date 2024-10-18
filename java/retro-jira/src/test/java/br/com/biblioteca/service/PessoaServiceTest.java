package br.com.biblioteca.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.biblioteca.dto.request.PessoaRequestDTO;
import br.com.biblioteca.dto.response.PessoaResponseDTO;
import br.com.biblioteca.model.PessoaModel;
import br.com.biblioteca.repository.PessoaRepository;

@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private PessoaService pessoaService;

    @Test
    void testBuscarGerentes() {
        PessoaModel gerente = PessoaModel.builder()
                .id(1L)
                .nome("Gerente 1")
                .gerente(true)
                .build();

        List<PessoaModel> gerentes = Collections.singletonList(gerente);

        when(pessoaRepository.findByGerenteTrue()).thenReturn(gerentes);

        List<PessoaResponseDTO> resultado = pessoaService.buscarGerentes();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Gerente 1", resultado.get(0).getNome());
        verify(pessoaRepository).findByGerenteTrue();
    }

    @Test
    void testBuscarFuncionarios() {
        PessoaModel funcionario = PessoaModel.builder()
                .id(2L)
                .nome("Funcionario 1")
                .funcionario(true)
                .build();

        List<PessoaModel> funcionarios = Collections.singletonList(funcionario);

        when(pessoaRepository.findByFuncionarioTrue()).thenReturn(funcionarios);

        List<PessoaResponseDTO> resultado = pessoaService.buscarFuncionarios();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Funcionario 1", resultado.get(0).getNome());
        verify(pessoaRepository).findByFuncionarioTrue();
    }

    @Test
    void testBuscarGerenteById_Success() {
        Long id = 1L;
        PessoaModel gerente = PessoaModel.builder()
                .id(1L)
                .nome("Gerente 1")
                .gerente(true)
                .build();

        when(pessoaRepository.findByGerenteTrueById(id)).thenReturn(Optional.of(gerente));

        PessoaModel resultado = pessoaService.buscarGerenteById(id);

        assertNotNull(resultado);
        assertEquals("Gerente 1", resultado.getNome());
        verify(pessoaRepository).findByGerenteTrueById(id);
    }

    @Test
    void testBuscarGerenteById_NotFound() {
        Long id = 1L;
        when(pessoaRepository.findByGerenteTrueById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> pessoaService.buscarGerenteById(id));
        assertEquals("Gerente não encontrado com o ID: " + id, exception.getMessage());
    }

    @Test
    void testBuscarMembrosByIdList() {
        List<Long> ids = Arrays.asList(1L, 2L);
        PessoaModel membro1 = PessoaModel.builder()
                .id(1L)
                .nome("Membro 1")
                .funcionario(true)
                .build();

        PessoaModel membro2 = PessoaModel.builder()
                .id(2L)
                .nome("Membro 2")
                .funcionario(true)
                .build();

        List<PessoaModel> membros = Arrays.asList(membro1, membro2);

        when(pessoaRepository.findByFuncionarioTrueByIds(ids)).thenReturn(membros);

        List<PessoaModel> resultado = pessoaService.buscarMembrosByIdList(ids);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(pessoaRepository).findByFuncionarioTrueByIds(ids);
    }

    @Test
    void testCadastrarPessoa() {
        PessoaRequestDTO pessoaDTO = new PessoaRequestDTO(
                "Maria Silva",
                "funcionario",
                "12345678901",
                LocalDate.now());

        PessoaModel pessoaModel = PessoaModel.builder()
                .nome(pessoaDTO.nome())
                .cpf(pessoaDTO.cpf())
                .dataNascimento(pessoaDTO.dataNascimento())
                .funcionario(true)
                .gerente(false)
                .build();

        when(pessoaRepository.saveAndFlush(any(PessoaModel.class))).thenReturn(pessoaModel);

        PessoaResponseDTO resultado = pessoaService.cadastrarPessoa(pessoaDTO);

        assertNotNull(resultado);
        assertEquals(pessoaDTO.nome(), resultado.getNome());
        assertEquals(pessoaDTO.cpf(), resultado.getCpf());
        verify(pessoaRepository).saveAndFlush(any(PessoaModel.class));
    }
}
