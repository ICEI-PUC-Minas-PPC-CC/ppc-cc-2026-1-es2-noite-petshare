package br.com.petshare.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SolicitacaoRequest(
        @NotNull Long donoId,
        @NotNull Long cuidadorId,
        @NotNull Long petId,
        @NotNull LocalDate dataInicio,
        @NotNull LocalDate dataFim,
        String observacoes
) {
}
