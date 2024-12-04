package io.github.mateus81.mensagensapi.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.mateus81.mensagensapi.model.entity.Contato;

public interface ContatoRepository extends JpaRepository<Contato, Integer> {
	
	@Query("SELECT c FROM Contato c JOIN c.usuario u WHERE u.id = :usuarioId")
	List<Contato> findByUsuarioId(Integer usuarioId);
	Optional<Contato> findByEmail(String email);
	@Query("SELECT COUNT(c) > 0 FROM Contato c WHERE c.usuario.id = :usuarioId AND c.nome = :nome")
	boolean existsByUsuarioIdAndNome(@Param("usuarioId") Integer usuarioId, @Param("nome") String nome);
}
