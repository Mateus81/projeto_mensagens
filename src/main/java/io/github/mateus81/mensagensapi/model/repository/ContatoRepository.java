package io.github.mateus81.mensagensapi.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.mateus81.mensagensapi.model.entity.Contato;

public interface ContatoRepository extends JpaRepository<Contato, Integer> {
	
	List<Contato> findByUsuarioId(Integer usuarioId);
	Optional<Contato> findByEmail(String email);
}
