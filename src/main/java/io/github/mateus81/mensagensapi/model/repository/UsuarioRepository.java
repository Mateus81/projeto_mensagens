package io.github.mateus81.mensagensapi.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.mateus81.mensagensapi.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	Usuario findByEmail(String email);
	
	Optional<Usuario> findOptionalByNome(String nome);
	
	Usuario findByNome(String nome);
}
