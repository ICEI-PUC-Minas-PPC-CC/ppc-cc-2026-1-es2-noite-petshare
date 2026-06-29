package br.com.petshare.service;

import br.com.petshare.dto.MensagemRequest;
import br.com.petshare.model.Mensagem;
import br.com.petshare.model.SolicitacaoCuidado;
import br.com.petshare.model.Usuario;
import br.com.petshare.repository.MensagemRepository;
import br.com.petshare.repository.SolicitacaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MensagemService {

    private final MensagemRepository mensagemRepository;
    private final SolicitacaoRepository solicitacaoRepository;
    private final UsuarioService usuarioService;

    public MensagemService(MensagemRepository mensagemRepository, SolicitacaoRepository solicitacaoRepository, UsuarioService usuarioService) {
        this.mensagemRepository = mensagemRepository;
        this.solicitacaoRepository = solicitacaoRepository;
        this.usuarioService = usuarioService;
    }

    public List<Mensagem> listar(Long solicitacaoId) {
        obterSolicitacao(solicitacaoId);
        return mensagemRepository.findBySolicitacaoIdOrderByDataHoraAsc(solicitacaoId);
    }

    public Mensagem criar(Long solicitacaoId, MensagemRequest request) {
        SolicitacaoCuidado solicitacao = obterSolicitacao(solicitacaoId);
        Usuario remetente = usuarioService.obterPorId(request.remetenteId());

        boolean participante = solicitacao.getDono().getId().equals(remetente.getId())
                || solicitacao.getCuidador().getId().equals(remetente.getId());
        if (!participante) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas dono ou cuidador podem enviar mensagens");
        }

        Mensagem mensagem = new Mensagem();
        mensagem.setSolicitacaoId(solicitacaoId);
        mensagem.setRemetente(remetente);
        mensagem.setConteudo(request.conteudo());
        mensagem.setDataHora(LocalDateTime.now());

        return mensagemRepository.save(mensagem);
    }

    private SolicitacaoCuidado obterSolicitacao(Long solicitacaoId) {
        return solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitacao nao encontrada"));
    }
}
