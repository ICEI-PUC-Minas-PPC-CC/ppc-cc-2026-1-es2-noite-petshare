package br.com.petshare.service;

import br.com.petshare.dto.AtualizarPerfilRequest;
import br.com.petshare.dto.CadastroUsuarioRequest;
import br.com.petshare.dto.LoginRequest;
import br.com.petshare.model.Usuario;
import br.com.petshare.model.UsuarioTipo;
import br.com.petshare.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario cadastrar(CadastroUsuarioRequest request) {
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ja cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(request.senha());
        usuario.setTelefone(request.telefone());
        usuario.setTipo(request.tipo());
        usuario.setDescricao(request.descricao());
        usuario.setDisponibilidade(request.disponibilidade());
        usuario.setFotoUrl(request.fotoUrl());
        usuario.setDataCadastro(LocalDateTime.now());

        return usuarioRepository.save(usuario);
    }

    public Usuario login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado"));

        if (!usuario.getSenha().equals(request.senha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha invalida");
        }

        return usuario;
    }

    public Usuario atualizarPerfil(Long id, AtualizarPerfilRequest request) {
        Usuario usuario = obterPorId(id);

        if (request.nome() != null && !request.nome().isBlank()) {
            usuario.setNome(request.nome());
        }
        if (request.telefone() != null) {
            usuario.setTelefone(request.telefone());
        }
        if (request.descricao() != null) {
            usuario.setDescricao(request.descricao());
        }
        if (request.disponibilidade() != null) {
            usuario.setDisponibilidade(request.disponibilidade());
        }
        if (request.fotoUrl() != null) {
            usuario.setFotoUrl(request.fotoUrl());
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario obterPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado"));
    }

    public List<Usuario> listarCuidadores() {
        return usuarioRepository.findByTipo(UsuarioTipo.CUIDADOR);
    }
}
