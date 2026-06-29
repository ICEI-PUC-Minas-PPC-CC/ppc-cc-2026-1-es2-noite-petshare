package br.com.petshare.repository;

import br.com.petshare.model.Usuario;
import br.com.petshare.model.UsuarioTipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    long countByTipo(UsuarioTipo tipo);

    List<Usuario> findByTipo(UsuarioTipo tipo);
}
