package br.com.petshare.dto;

import br.com.petshare.model.UsuarioTipo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastroUsuarioRequest(
        @NotBlank String nome,
        @Email @NotBlank String email,
        @NotBlank String senha,
        String telefone,
        @NotNull UsuarioTipo tipo,
        String descricao,
        String disponibilidade,
        String fotoUrl
) {
}
