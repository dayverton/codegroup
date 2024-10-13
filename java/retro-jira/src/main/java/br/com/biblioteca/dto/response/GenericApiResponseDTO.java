package br.com.biblioteca.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenericApiResponseDTO<T> {
    private boolean success;
    private T data;
    private String error;
}
