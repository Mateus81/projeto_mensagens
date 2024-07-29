package io.github.mateus81.mensagensapi.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.mateus81.mensagensapi.model.entity.Conversa;
import io.github.mateus81.mensagensapi.model.entity.Usuario;

public interface ConversaRepository extends JpaRepository<Conversa, Integer> {

	List<Conversa> findByUsuario(Usuario usuario);
	List<Conversa> findByUsuarioOrUsuarioDest(Usuario usuario, Usuario usuarioDest);

}
