package br.com.petshare.service;

import br.com.petshare.dto.CadastroUsuarioRequest;
import br.com.petshare.dto.LoginRequest;
import br.com.petshare.model.Usuario;
import br.com.petshare.model.UsuarioTipo;
import br.com.petshare.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void cadastrar_deveSalvarUsuarioQuandoEmailNaoExiste() {
        CadastroUsuarioRequest request = new CadastroUsuarioRequest(
                "Ana",
                "ana@email.com",
                "123456",
                "99999-9999",
                UsuarioTipo.CUIDADOR,
                "Cuido pets",
                "Todos os dias",
                null
        );

        when(usuarioRepository.findByEmail("ana@email.com")).thenReturn(Optional.empty());

        Usuario usuarioPersistido = new Usuario();
        usuarioPersistido.setId(1L);
        usuarioPersistido.setNome(request.nome());
        usuarioPersistido.setEmail(request.email());
        usuarioPersistido.setSenha(request.senha());
        usuarioPersistido.setTipo(request.tipo());
        usuarioPersistido.setDataCadastro(LocalDateTime.now());

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioPersistido);

        Usuario resultado = usuarioService.cadastrar(request);

        assertNotNull(resultado);
        assertEquals("Ana", resultado.getNome());
        assertEquals("ana@email.com", resultado.getEmail());
        verify(usuarioRepository).findByEmail("ana@email.com");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void cadastrar_deveFalharQuandoEmailJaCadastrado() {
        CadastroUsuarioRequest request = new CadastroUsuarioRequest(
                "Ana",
                "ana@email.com",
                "123456",
                "99999-9999",
                UsuarioTipo.CUIDADOR,
                "Cuido pets",
                "Todos os dias",
                null
        );

        when(usuarioRepository.findByEmail("ana@email.com")).thenReturn(Optional.of(new Usuario()));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> usuarioService.cadastrar(request));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void login_deveRetornarUsuarioQuandoCredenciaisSaoValidas() {
        Usuario usuario = new Usuario();
        usuario.setId(7L);
        usuario.setEmail("ana@email.com");
        usuario.setSenha("123456");

        LoginRequest request = new LoginRequest("ana@email.com", "123456");
        when(usuarioRepository.findByEmail("ana@email.com")).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.login(request);

        assertNotNull(resultado);
        assertEquals(7L, resultado.getId());
    }

    @Test
    void login_deveFalharQuandoSenhaEstaInvalida() {
        Usuario usuario = new Usuario();
        usuario.setEmail("ana@email.com");
        usuario.setSenha("senha-correta");

        LoginRequest request = new LoginRequest("ana@email.com", "senha-errada");
        when(usuarioRepository.findByEmail("ana@email.com")).thenReturn(Optional.of(usuario));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> usuarioService.login(request));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }
}
