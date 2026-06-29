package br.com.petshare;

import br.com.petshare.model.Usuario;
import br.com.petshare.model.UsuarioTipo;
import br.com.petshare.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class PetshareApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetshareApplication.class, args);
    }

    @Bean
    CommandLineRunner seedCuidadores(UsuarioRepository usuarioRepository) {
        return args -> {
            if (usuarioRepository.countByTipo(UsuarioTipo.CUIDADOR) == 0) {
                usuarioRepository.save(new Usuario(null, "Lucas Almeida", "lucas@petshare.com", "123456", "31990000001", LocalDateTime.now(), UsuarioTipo.CUIDADOR, "Estudante de veterinaria", "Turno noturno", "https://picsum.photos/seed/lucas/120"));
                usuarioRepository.save(new Usuario(null, "Beatriz Costa", "bia@petshare.com", "123456", "31990000002", LocalDateTime.now(), UsuarioTipo.CUIDADOR, "Cuidadora com 4 anos de experiencia", "Finais de semana", "https://picsum.photos/seed/bia/120"));
            }
        };
    }
}
