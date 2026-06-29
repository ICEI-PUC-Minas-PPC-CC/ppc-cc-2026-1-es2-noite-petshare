package br.com.petshare.repository;

import br.com.petshare.model.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
    List<Mensagem> findBySolicitacaoIdOrderByDataHoraAsc(Long solicitacaoId);
}
