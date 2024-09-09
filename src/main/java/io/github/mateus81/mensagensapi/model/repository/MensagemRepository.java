package io.github.mateus81.mensagensapi.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.mateus81.mensagensapi.model.entity.Mensagem;

public interface MensagemRepository extends JpaRepository<Mensagem, Integer> {

	List<Mensagem> findByConversaId(Integer conversaId);
	@Query("SELECT m FROM Mensagem m JOIN FETCH m.conversa WHERE m.id = :id")
	Optional<Mensagem> findByIdWithConversa(@Param("id")Integer id);
}
