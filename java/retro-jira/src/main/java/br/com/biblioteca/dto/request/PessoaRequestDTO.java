package br.com.biblioteca.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PessoaRequestDTO(
        @NotBlank @Size(max = 50) String nome,
        @NotBlank @Size(max = 15) String atribuicao,
        @NotBlank @Size(min = 11, max = 11) String cpf,
        @NotNull LocalDate dataNascimento) {

}
