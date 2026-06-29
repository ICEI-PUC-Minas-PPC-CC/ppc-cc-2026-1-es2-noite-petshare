package br.com.petshare.dto;

import br.com.petshare.model.SolicitacaoStatus;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusRequest(
        @NotNull Long cuidadorId,
        @NotNull SolicitacaoStatus status
) {
}
