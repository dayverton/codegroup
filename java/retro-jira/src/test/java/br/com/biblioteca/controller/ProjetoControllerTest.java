package br.com.biblioteca.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.biblioteca.dto.request.ProjetoRequestDTO;
import br.com.biblioteca.dto.response.ProjetoResponseDTO;
import br.com.biblioteca.service.ProjetoService;
import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
class ProjetoControllerTest {

    @Mock
    private ProjetoService projetoService;

    @InjectMocks
    private ProjetoController projetoController;

    @Mock
    private Model model;

    @Mock
    private HttpServletRequest request;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Test
    void testHome() {
        when(projetoService.listarTodosProjetos()).thenReturn(Collections.emptyList());

        String viewName = projetoController.home(model, request);

        assertEquals("index", viewName);
        verify(model).addAttribute(eq("projetos"), anyList());
    }

    @Test
    void testSalvarProjeto_Success() {
        ProjetoRequestDTO projetoDTO = new ProjetoRequestDTO(
                null,
                "Projeto Teste",
                LocalDate.now(),
                1L,
                "EM_ANDAMENTO",
                "BAIXO",
                LocalDate.now().plusDays(30),
                null,
                1000.0f,
                "Descrição",
                Collections.emptyList());

        String result = projetoController.salvarProjeto(projetoDTO, redirectAttributes);

        assertEquals("redirect:/projetos", result);
        verify(projetoService).salvarProjeto(projetoDTO);
    }

    @Test
    void testSalvarProjeto_Exception() {
        ProjetoRequestDTO projetoDTO = new ProjetoRequestDTO(
                null,
                "Projeto Teste",
                LocalDate.now(),
                1L,
                "EM_ANDAMENTO",
                "BAIXO",
                LocalDate.now().plusDays(30),
                null,
                1000.0f,
                "Descrição",
                Collections.emptyList());

        String errorMessage = "Erro ao salvar projeto";
        doThrow(new RuntimeException(errorMessage)).when(projetoService).salvarProjeto(projetoDTO);

        String result = projetoController.salvarProjeto(projetoDTO, redirectAttributes);

        assertEquals("redirect:/projetos", result);
        verify(redirectAttributes).addFlashAttribute("erroMensagem", errorMessage);
    }

    @Test
    void testExcluirProjeto_Success() {
        Long id = 1L;

        String result = projetoController.excluirProjeto(id, redirectAttributes);

        assertEquals("redirect:/projetos", result);
        verify(projetoService).excluirProjeto(id);
    }

    @Test
    void testExcluirProjeto_Exception() {
        Long id = 1L;
        String errorMessage = "Erro ao excluir projeto";
        doThrow(new RuntimeException(errorMessage)).when(projetoService).excluirProjeto(id);

        String result = projetoController.excluirProjeto(id, redirectAttributes);

        assertEquals("redirect:/projetos", result);
        verify(redirectAttributes).addFlashAttribute("erroMensagem", errorMessage);
    }

    @Test
    void testBuscarProjetoPorId() {
        Long id = 1L;
        ProjetoResponseDTO responseDTO = ProjetoResponseDTO.builder().id(id).nome("Projeto Teste").build();
        when(projetoService.buscarProjetoPorId(id)).thenReturn(responseDTO);

        ProjetoResponseDTO result = projetoController.buscarProjetoPorId(id);

        assertEquals(responseDTO, result);
    }

    @Test
    void testPesquisarProjetos() {
        String nome = "Teste";
        when(projetoService.buscarProjetosPorNome(nome)).thenReturn(Collections.emptyList());

        String viewName = projetoController.pesquisarProjetos(nome, model);

        assertEquals("fragments/projetos", viewName);
        verify(model).addAttribute(eq("projetos"), anyList());
    }

    @Test
    void testPesquisarProjetos_ListarTodosProjetos() {
        String nome = "";
        List<ProjetoResponseDTO> projetos = Arrays.asList(
                ProjetoResponseDTO.builder().id(1L).nome("Projeto A").build(),
                ProjetoResponseDTO.builder().id(2L).nome("Projeto B").build());

        when(projetoService.listarTodosProjetos()).thenReturn(projetos);

        String viewName = projetoController.pesquisarProjetos(nome, model);

        assertEquals("fragments/projetos", viewName);
        verify(model).addAttribute("projetos", projetos);
        verify(projetoService).listarTodosProjetos();
    }
}
