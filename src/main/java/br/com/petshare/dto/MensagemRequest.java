package br.com.petshare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MensagemRequest(
        @NotNull Long remetenteId,
        @NotBlank String conteudo
) {
}
