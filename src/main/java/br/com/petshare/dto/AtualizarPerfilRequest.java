package br.com.petshare.dto;

public record AtualizarPerfilRequest(
        String nome,
        String telefone,
        String descricao,
        String disponibilidade,
        String fotoUrl
) {
}
