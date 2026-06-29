package br.com.petshare.service;

import br.com.petshare.dto.AtualizarStatusRequest;
import br.com.petshare.dto.SolicitacaoRequest;
import br.com.petshare.model.*;
import br.com.petshare.repository.SolicitacaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final UsuarioService usuarioService;
    private final PetService petService;
    private final NotificacaoService notificacaoService;

    public SolicitacaoService(SolicitacaoRepository solicitacaoRepository, UsuarioService usuarioService, PetService petService, NotificacaoService notificacaoService) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.usuarioService = usuarioService;
        this.petService = petService;
        this.notificacaoService = notificacaoService;
    }

    public SolicitacaoCuidado criar(SolicitacaoRequest request) {
        Usuario dono = usuarioService.obterPorId(request.donoId());
        Usuario cuidador = usuarioService.obterPorId(request.cuidadorId());
        Pet pet = petService.obterPorId(request.petId());

        if (dono.getTipo() != UsuarioTipo.DONO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario informado como dono nao e do tipo DONO");
        }

        if (cuidador.getTipo() != UsuarioTipo.CUIDADOR) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario informado como cuidador nao e do tipo CUIDADOR");
        }

        if (!pet.getDono().getId().equals(dono.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pet nao pertence ao dono informado");
        }

        if (request.dataFim().isBefore(request.dataInicio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data fim nao pode ser anterior a data inicio");
        }

        SolicitacaoCuidado solicitacao = new SolicitacaoCuidado();
        solicitacao.setDono(dono);
        solicitacao.setCuidador(cuidador);
        solicitacao.setPet(pet);
        solicitacao.setDataInicio(request.dataInicio());
        solicitacao.setDataFim(request.dataFim());
        solicitacao.setObservacoes(request.observacoes());
        solicitacao.setStatus(SolicitacaoStatus.PENDENTE);

        SolicitacaoCuidado salva = solicitacaoRepository.save(solicitacao);
        notificacaoService.criar(cuidador, "Nova solicitacao recebida para o pet " + pet.getNome(), salva.getId());
        return salva;
    }

    public List<SolicitacaoCuidado> listarPorUsuario(Long usuarioId) {
        return solicitacaoRepository.findByDonoIdOrCuidadorId(usuarioId, usuarioId);
    }

    public SolicitacaoCuidado atualizarStatus(Long solicitacaoId, AtualizarStatusRequest request) {
        SolicitacaoCuidado solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitacao nao encontrada"));

        if (!solicitacao.getCuidador().getId().equals(request.cuidadorId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Somente o cuidador da solicitacao pode alterar o status");
        }

        if (request.status() != SolicitacaoStatus.ACEITA && request.status() != SolicitacaoStatus.RECUSADA) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status permitido apenas: ACEITA ou RECUSADA");
        }

        solicitacao.setStatus(request.status());
        SolicitacaoCuidado atualizada = solicitacaoRepository.save(solicitacao);
        notificacaoService.criar(
                solicitacao.getDono(),
                "Sua solicitacao para o pet " + solicitacao.getPet().getNome() + " foi " + request.status(),
                solicitacao.getId()
        );

        return atualizada;
    }
}
