package br.com.biblioteca.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.biblioteca.dto.request.PessoaRequestDTO;
import br.com.biblioteca.dto.response.GenericApiResponseDTO;
import br.com.biblioteca.dto.response.PessoaResponseDTO;
import br.com.biblioteca.service.PessoaService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/pessoas")
public class PessoaController {

    private final PessoaService pessoaService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PessoaResponseDTO.class))
            }),
            @ApiResponse(responseCode = "400", content = @Content),
            @ApiResponse(responseCode = "500", content = @Content)
    })
    @GetMapping("/funcionarios/buscar")
    public ResponseEntity<GenericApiResponseDTO<List<PessoaResponseDTO>>> buscarFuncionarios() {
        GenericApiResponseDTO<List<PessoaResponseDTO>> response = new GenericApiResponseDTO<>();

        try {
            List<PessoaResponseDTO> pessoaLista = pessoaService.buscarFuncionarios();

            response.setSuccess(true);
            response.setData(pessoaLista);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setError(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PessoaResponseDTO.class))
            }),
            @ApiResponse(responseCode = "400", content = @Content),
            @ApiResponse(responseCode = "500", content = @Content)
    })
    @GetMapping("/gerentes/buscar")
    public ResponseEntity<GenericApiResponseDTO<List<PessoaResponseDTO>>> buscarGerentes() {
        GenericApiResponseDTO<List<PessoaResponseDTO>> response = new GenericApiResponseDTO<>();

        try {
            List<PessoaResponseDTO> pessoaLista = pessoaService.buscarGerentes();

            response.setSuccess(true);
            response.setData(pessoaLista);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setError(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PessoaResponseDTO.class))
            }),
            @ApiResponse(responseCode = "400", content = @Content),
            @ApiResponse(responseCode = "500", content = @Content)
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<GenericApiResponseDTO<PessoaResponseDTO>> cadastrarPessoa(
            @RequestBody @Valid PessoaRequestDTO pessoaDTO, BindingResult bindingResult) {
        GenericApiResponseDTO<PessoaResponseDTO> response = new GenericApiResponseDTO<>();

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));

            response.setSuccess(false);
            response.setError(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            PessoaResponseDTO pessoa = pessoaService.cadastrarPessoa(pessoaDTO);

            response.setSuccess(true);
            response.setData(pessoa);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setError(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }
}
