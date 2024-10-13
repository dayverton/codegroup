package br.com.biblioteca.dto.response;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PessoaResponseDTO {
    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private String cpf;
    private boolean funcionario;
    private boolean gerente;
}
