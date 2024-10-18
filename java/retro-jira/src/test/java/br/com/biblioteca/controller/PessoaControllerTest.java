package br.com.biblioteca.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.biblioteca.dto.request.PessoaRequestDTO;
import br.com.biblioteca.dto.response.GenericApiResponseDTO;
import br.com.biblioteca.dto.response.PessoaResponseDTO;
import br.com.biblioteca.service.PessoaService;
import br.com.biblioteca.utils.ObjectMapperUtil;

class PessoaControllerTest {

    @Mock
    private PessoaService pessoaService;

    @InjectMocks
    private PessoaController pessoaController;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pessoaController).build();
        objectMapper = ObjectMapperUtil.getInstance();
    }

    @Test
    void testBuscarFuncionarios_Success() throws Exception {

        List<PessoaResponseDTO> funcionarios = List.of(
                PessoaResponseDTO.builder()
                        .id(1L)
                        .nome("João Silva")
                        .dataNascimento(LocalDate.of(1980, 1, 1))
                        .cpf("12345678901")
                        .funcionario(true)
                        .gerente(false)
                        .build());

        when(pessoaService.buscarFuncionarios()).thenReturn(funcionarios);

        mockMvc.perform(get("/api/v1/pessoas/funcionarios/buscar")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].nome").value("João Silva"));

        verify(pessoaService, times(1)).buscarFuncionarios();
    }

    @Test
    void testBuscarFuncionarios_Exception() throws Exception {

        when(pessoaService.buscarFuncionarios()).thenThrow(new RuntimeException("Erro ao buscar funcionários"));

        mockMvc.perform(get("/api/v1/pessoas/funcionarios/buscar")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Erro ao buscar funcionários"));

        verify(pessoaService, times(1)).buscarFuncionarios();
    }

    @Test
    void testCadastrarPessoa_Success() throws Exception {

        PessoaRequestDTO pessoaRequest = new PessoaRequestDTO(
                "Maria Silva",
                "funcionario",
                "12345678901",
                LocalDate.now());

        PessoaResponseDTO pessoaResponse = PessoaResponseDTO.builder()
                .id(1L)
                .nome("Maria Silva")
                .cpf("12345678901")
                .dataNascimento(LocalDate.of(1990, 5, 15))
                .funcionario(true)
                .gerente(false)
                .build();

        when(pessoaService.cadastrarPessoa(any(PessoaRequestDTO.class))).thenReturn(pessoaResponse);

        mockMvc.perform(post("/api/v1/pessoas/cadastrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pessoaRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nome").value("Maria Silva"));

        verify(pessoaService, times(1)).cadastrarPessoa(any(PessoaRequestDTO.class));
    }

    @SuppressWarnings("null")
    @Test
    void testCadastrarPessoa_Exception() {
        PessoaRequestDTO requestDTO = new PessoaRequestDTO(
                "Nome",
                "funcionario",
                "12345678901",
                LocalDate.now());

        BindingResult bindingResult = mock(BindingResult.class);
        String errorMessage = "Erro ao cadastrar pessoa";

        when(bindingResult.hasErrors()).thenReturn(false);
        when(pessoaService.cadastrarPessoa(requestDTO)).thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<GenericApiResponseDTO<PessoaResponseDTO>> response = pessoaController.cadastrarPessoa(requestDTO,
                bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals(errorMessage, response.getBody().getError());
    }

    @Test
    void testCadastrarPessoa_ValidationErrors() throws Exception {

        PessoaRequestDTO pessoaRequest = new PessoaRequestDTO(
                "",
                "",
                "123",
                null);

        mockMvc.perform(post("/api/v1/pessoas/cadastrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pessoaRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").exists());

        verify(pessoaService, times(0)).cadastrarPessoa(any(PessoaRequestDTO.class));
    }

    @Test
    void testBuscarGerentes_Success() throws Exception {

        List<PessoaResponseDTO> gerentes = List.of(
                PessoaResponseDTO.builder()
                        .id(1L)
                        .nome("Carlos Gerente")
                        .dataNascimento(LocalDate.of(1985, 3, 20))
                        .cpf("09876543210")
                        .funcionario(false)
                        .gerente(true)
                        .build());

        when(pessoaService.buscarGerentes()).thenReturn(gerentes);

        mockMvc.perform(get("/api/v1/pessoas/gerentes/buscar")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].nome").value("Carlos Gerente"));

        verify(pessoaService, times(1)).buscarGerentes();
    }

    @Test
    void testBuscarGerentes_Exception() throws Exception {

        when(pessoaService.buscarGerentes()).thenThrow(new RuntimeException("Erro ao buscar gerentes"));

        mockMvc.perform(get("/api/v1/pessoas/gerentes/buscar")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Erro ao buscar gerentes"));

        verify(pessoaService, times(1)).buscarGerentes();
    }

}
