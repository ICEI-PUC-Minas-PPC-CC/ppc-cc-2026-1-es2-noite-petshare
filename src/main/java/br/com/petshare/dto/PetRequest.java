package br.com.petshare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PetRequest(
        @NotNull Long donoId,
        @NotBlank String nome,
        @NotBlank String especie,
        String raca,
        Integer idade,
        String necessidadesEspeciais,
        String historico
) {
}
