package br.com.petshare.controller;

import br.com.petshare.dto.PetRequest;
import br.com.petshare.model.Pet;
import br.com.petshare.service.PetService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping("/pets")
    public Pet cadastrar(@Valid @RequestBody PetRequest request) {
        return petService.cadastrar(request);
    }

    @GetMapping("/usuarios/{donoId}/pets")
    public List<Pet> listarPorDono(@PathVariable Long donoId) {
        return petService.listarPorDono(donoId);
    }
}
