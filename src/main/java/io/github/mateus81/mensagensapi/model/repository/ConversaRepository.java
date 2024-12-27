package io.github.mateus81.mensagensapi.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.mateus81.mensagensapi.model.entity.Conversa;
import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.service.ConversaService.StatusConversa;

public interface ConversaRepository extends JpaRepository<Conversa, Integer> {

	// Busca lista de conversa por usuário
	List<Conversa> findByUsuario(Usuario usuario);
	// Busca lista de conversas por usuário (remetente/destinatário) de status aberto 
	List<Conversa> findByUsuarioOrUsuarioDestAndStatus(Usuario usuario, Usuario usuarioDest, StatusConversa status);

}
