package br.com.petshare.service;

import br.com.petshare.model.Notificacao;
import br.com.petshare.model.Usuario;
import br.com.petshare.repository.NotificacaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;

    public NotificacaoService(NotificacaoRepository notificacaoRepository) {
        this.notificacaoRepository = notificacaoRepository;
    }

    public void criar(Usuario usuario, String mensagem, Long solicitacaoId) {
        Notificacao notificacao = new Notificacao();
        notificacao.setUsuario(usuario);
        notificacao.setMensagem(mensagem);
        notificacao.setSolicitacaoId(solicitacaoId);
        notificacao.setDataHora(LocalDateTime.now());
        notificacao.setLida(false);
        notificacaoRepository.save(notificacao);
    }

    public List<Notificacao> listarPorUsuario(Long usuarioId) {
        return notificacaoRepository.findByUsuarioIdOrderByDataHoraDesc(usuarioId);
    }

    public Notificacao marcarComoLida(Long notificacaoId) {
        Notificacao notificacao = notificacaoRepository.findById(notificacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notificacao nao encontrada"));
        notificacao.setLida(true);
        return notificacaoRepository.save(notificacao);
    }
}
