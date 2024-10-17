package br.com.biblioteca.dto.request;

import java.time.LocalDate;
import java.util.List;

public record ProjetoRequestDTO(
        Long id,
        String nome,
        LocalDate dataInicio,
        Long gerente,
        String status,
        String risco,
        LocalDate dataPrevisaoFim,
        LocalDate dataFim,
        float orcamento,
        String descricao,
        List<Long> membros) {
}
