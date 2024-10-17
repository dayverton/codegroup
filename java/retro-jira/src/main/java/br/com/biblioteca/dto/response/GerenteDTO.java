package br.com.biblioteca.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GerenteDTO {
    private Long id;
    private String nome;
}
