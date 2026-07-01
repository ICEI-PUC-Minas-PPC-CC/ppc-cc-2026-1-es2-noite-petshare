package br.com.petshare.service;

import br.com.petshare.dto.PetRequest;
import br.com.petshare.model.Pet;
import br.com.petshare.model.Usuario;
import br.com.petshare.model.UsuarioTipo;
import br.com.petshare.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private PetService petService;

    @Test
    void cadastrar_deveSalvarPetQuandoDonoEhValido() {
        Usuario dono = new Usuario();
        dono.setId(1L);
        dono.setTipo(UsuarioTipo.DONO);

        PetRequest request = new PetRequest(1L, "Rex", "Cachorro", "Vira-lata", 3, null, "Muito brincalhao");

        when(usuarioService.obterPorId(1L)).thenReturn(dono);

        Pet petSalvo = new Pet();
        petSalvo.setId(10L);
        petSalvo.setNome("Rex");
        petSalvo.setDono(dono);

        when(petRepository.save(any(Pet.class))).thenReturn(petSalvo);

        Pet resultado = petService.cadastrar(request);

        assertNotNull(resultado);
        assertEquals("Rex", resultado.getNome());
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    void cadastrar_deveFalharQuandoUsuarioNaoEhDono() {
        Usuario cuidador = new Usuario();
        cuidador.setId(2L);
        cuidador.setTipo(UsuarioTipo.CUIDADOR);

        PetRequest request = new PetRequest(2L, "Rex", "Cachorro", "Vira-lata", 2, null, null);

        when(usuarioService.obterPorId(2L)).thenReturn(cuidador);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> petService.cadastrar(request));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}
