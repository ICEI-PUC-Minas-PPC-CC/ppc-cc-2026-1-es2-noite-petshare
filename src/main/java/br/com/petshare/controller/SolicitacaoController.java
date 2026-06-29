package br.com.petshare.controller;

import br.com.petshare.dto.AtualizarStatusRequest;
import br.com.petshare.dto.SolicitacaoRequest;
import br.com.petshare.model.SolicitacaoCuidado;
import br.com.petshare.service.SolicitacaoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    public SolicitacaoController(SolicitacaoService solicitacaoService) {
        this.solicitacaoService = solicitacaoService;
    }

    @PostMapping("/solicitacoes")
    public SolicitacaoCuidado criar(@Valid @RequestBody SolicitacaoRequest request) {
        return solicitacaoService.criar(request);
    }

    @GetMapping("/solicitacoes")
    public List<SolicitacaoCuidado> listar(@RequestParam Long usuarioId) {
        return solicitacaoService.listarPorUsuario(usuarioId);
    }

    @PatchMapping("/solicitacoes/{id}/status")
    public SolicitacaoCuidado atualizarStatus(@PathVariable Long id, @Valid @RequestBody AtualizarStatusRequest request) {
        return solicitacaoService.atualizarStatus(id, request);
    }
}
