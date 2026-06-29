package br.com.petshare.repository;

import br.com.petshare.model.SolicitacaoCuidado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitacaoRepository extends JpaRepository<SolicitacaoCuidado, Long> {
    List<SolicitacaoCuidado> findByDonoIdOrCuidadorId(Long donoId, Long cuidadorId);
}
