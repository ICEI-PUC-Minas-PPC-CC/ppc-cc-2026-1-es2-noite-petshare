package br.com.petshare.controller;

import br.com.petshare.model.Notificacao;
import br.com.petshare.service.NotificacaoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    public NotificacaoController(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    @GetMapping("/usuarios/{usuarioId}/notificacoes")
    public List<Notificacao> listar(@PathVariable Long usuarioId) {
        return notificacaoService.listarPorUsuario(usuarioId);
    }

    @PatchMapping("/notificacoes/{id}/lida")
    public Notificacao marcarComoLida(@PathVariable Long id) {
        return notificacaoService.marcarComoLida(id);
    }
}
