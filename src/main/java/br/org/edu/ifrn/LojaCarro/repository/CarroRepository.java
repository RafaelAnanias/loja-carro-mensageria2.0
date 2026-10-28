package br.org.edu.ifrn.LojaCarro.repository;

import br.org.edu.ifrn.LojaCarro.model.Carro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarroRepository extends JpaRepository<Carro, Long> {
    Optional<Carro> findFirstByModelo(String modelo);
}