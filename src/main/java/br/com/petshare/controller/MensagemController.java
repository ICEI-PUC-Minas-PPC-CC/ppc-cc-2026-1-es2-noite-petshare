package br.com.petshare.controller;

import br.com.petshare.dto.MensagemRequest;
import br.com.petshare.model.Mensagem;
import br.com.petshare.service.MensagemService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MensagemController {

    private final MensagemService mensagemService;

    public MensagemController(MensagemService mensagemService) {
        this.mensagemService = mensagemService;
    }

    @GetMapping("/solicitacoes/{solicitacaoId}/mensagens")
    public List<Mensagem> listar(@PathVariable Long solicitacaoId) {
        return mensagemService.listar(solicitacaoId);
    }

    @PostMapping("/solicitacoes/{solicitacaoId}/mensagens")
    public Mensagem enviar(@PathVariable Long solicitacaoId, @Valid @RequestBody MensagemRequest request) {
        return mensagemService.criar(solicitacaoId, request);
    }
}
