package br.com.petshare.controller;

import br.com.petshare.dto.AtualizarPerfilRequest;
import br.com.petshare.dto.CadastroUsuarioRequest;
import br.com.petshare.dto.LoginRequest;
import br.com.petshare.model.Usuario;
import br.com.petshare.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/usuarios/cadastro")
    public Usuario cadastrar(@Valid @RequestBody CadastroUsuarioRequest request) {
        return usuarioService.cadastrar(request);
    }

    @PostMapping("/usuarios/login")
    public Usuario login(@Valid @RequestBody LoginRequest request) {
        return usuarioService.login(request);
    }

    @GetMapping("/usuarios/{id}")
    public Usuario buscarPorId(@PathVariable Long id) {
        return usuarioService.obterPorId(id);
    }

    @PutMapping("/usuarios/{id}")
    public Usuario atualizar(@PathVariable Long id, @RequestBody AtualizarPerfilRequest request) {
        return usuarioService.atualizarPerfil(id, request);
    }

    @GetMapping("/cuidadores")
    public List<Usuario> listarCuidadores() {
        return usuarioService.listarCuidadores();
    }

    @GetMapping("/cuidadores/{id}")
    public Usuario buscarCuidador(@PathVariable Long id) {
        return usuarioService.obterPorId(id);
    }
}
