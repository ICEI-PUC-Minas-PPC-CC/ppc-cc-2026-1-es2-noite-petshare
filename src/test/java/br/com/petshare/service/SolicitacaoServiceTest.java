package br.com.petshare.service;

import br.com.petshare.dto.AtualizarStatusRequest;
import br.com.petshare.dto.SolicitacaoRequest;
import br.com.petshare.model.Pet;
import br.com.petshare.model.SolicitacaoCuidado;
import br.com.petshare.model.SolicitacaoStatus;
import br.com.petshare.model.Usuario;
import br.com.petshare.model.UsuarioTipo;
import br.com.petshare.repository.SolicitacaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitacaoServiceTest {

    @Mock
    private SolicitacaoRepository solicitacaoRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private PetService petService;

    @Mock
    private NotificacaoService notificacaoService;

    @InjectMocks
    private SolicitacaoService solicitacaoService;

    @Test
    void criar_deveSalvarSolicitacaoQuandoDadosSaoValidos() {
        Usuario dono = criarUsuario(1L, UsuarioTipo.DONO);
        Usuario cuidador = criarUsuario(2L, UsuarioTipo.CUIDADOR);
        Pet pet = new Pet();
        pet.setId(10L);
        pet.setNome("Rex");
        pet.setDono(dono);

        SolicitacaoRequest request = new SolicitacaoRequest(
                1L,
                2L,
                10L,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 5),
                "Cuidados especiais"
        );

        when(usuarioService.obterPorId(1L)).thenReturn(dono);
        when(usuarioService.obterPorId(2L)).thenReturn(cuidador);
        when(petService.obterPorId(10L)).thenReturn(pet);

        SolicitacaoCuidado solicitacaoSalva = new SolicitacaoCuidado();
        solicitacaoSalva.setId(99L);
        solicitacaoSalva.setDono(dono);
        solicitacaoSalva.setCuidador(cuidador);
        solicitacaoSalva.setPet(pet);
        solicitacaoSalva.setStatus(SolicitacaoStatus.PENDENTE);
        when(solicitacaoRepository.save(any(SolicitacaoCuidado.class))).thenReturn(solicitacaoSalva);

        SolicitacaoCuidado resultado = solicitacaoService.criar(request);

        assertNotNull(resultado);
        assertEquals(SolicitacaoStatus.PENDENTE, resultado.getStatus());
        verify(notificacaoService).criar(eq(cuidador), anyString(), eq(99L));
    }

    @Test
    void criar_deveFalharQuandoDataFimEhAnteriorAoInicio() {
        Usuario dono = criarUsuario(1L, UsuarioTipo.DONO);
        Usuario cuidador = criarUsuario(2L, UsuarioTipo.CUIDADOR);
        Pet pet = new Pet();
        pet.setId(10L);
        pet.setNome("Rex");
        pet.setDono(dono);

        SolicitacaoRequest request = new SolicitacaoRequest(
                1L,
                2L,
                10L,
                LocalDate.of(2026, 7, 10),
                LocalDate.of(2026, 7, 5),
                "Erro de datas"
        );

        when(usuarioService.obterPorId(1L)).thenReturn(dono);
        when(usuarioService.obterPorId(2L)).thenReturn(cuidador);
        when(petService.obterPorId(10L)).thenReturn(pet);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> solicitacaoService.criar(request));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(notificacaoService, never()).criar(any(), anyString(), anyLong());
    }

    private Usuario criarUsuario(Long id, UsuarioTipo tipo) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setTipo(tipo);
        return usuario;
    }
}
