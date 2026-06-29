package br.com.petshare.service;

import br.com.petshare.dto.PetRequest;
import br.com.petshare.model.Pet;
import br.com.petshare.model.Usuario;
import br.com.petshare.model.UsuarioTipo;
import br.com.petshare.repository.PetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final UsuarioService usuarioService;

    public PetService(PetRepository petRepository, UsuarioService usuarioService) {
        this.petRepository = petRepository;
        this.usuarioService = usuarioService;
    }

    public Pet cadastrar(PetRequest request) {
        Usuario dono = usuarioService.obterPorId(request.donoId());
        if (dono.getTipo() != UsuarioTipo.DONO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Somente donos podem cadastrar pets");
        }

        Pet pet = new Pet();
        pet.setDono(dono);
        pet.setNome(request.nome());
        pet.setEspecie(request.especie());
        pet.setRaca(request.raca());
        pet.setIdade(request.idade());
        pet.setNecessidadesEspeciais(request.necessidadesEspeciais());
        pet.setHistorico(request.historico());

        return petRepository.save(pet);
    }

    public List<Pet> listarPorDono(Long donoId) {
        return petRepository.findByDonoId(donoId);
    }

    public Pet obterPorId(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet nao encontrado"));
    }
}
