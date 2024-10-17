package br.com.biblioteca.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjetoResponseDTO {
    private Long id;
    private String nome;
    private LocalDate dataInicio;
    private GerenteDTO gerente;
    private String status;
    private String risco;
    private LocalDate dataPrevisaoFim;
    private LocalDate dataFim;
    private float orcamento;
    private String descricao;
    private List<Long> membros;
}
