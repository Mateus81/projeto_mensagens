package io.github.mateus81.mensagensapi.model.service;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.mateus81.mensagensapi.model.entity.Mensagem;
import io.github.mateus81.mensagensapi.model.repository.MensagemRepository;

@Service
public class MensagemService {

	private final MensagemRepository mensagemRepository;
	
	@Autowired
	private EntityManager entityManager;

	// Construtor
	public MensagemService(MensagemRepository mensagemRepository) {
		this.mensagemRepository = mensagemRepository;
	}

	// Marca mensagem como lida
	public void markAsRead(Integer mensagemId) {
		Mensagem mensagem = mensagemRepository.findById(mensagemId)
				.orElseThrow(() -> new RuntimeException("Mensagem não encontrada"));
		if (!mensagem.isVista()) {
			mensagem.setVista(true);
			mensagemRepository.save(mensagem);
		}
	}

	// Marca todas as mensagens como lidas
	@Transactional
	public void markAllAsRead(Integer conversaId) {
		// Consulta com Java Persistency QUERY LANGUAGE
		String jpql = "UPDATE Mensagem m SET m.vista = true WHERE m.conversa.id = conversaId";
		// Executa
		Query query = entityManager.createQuery(jpql);
		query.setParameter("conversaId", conversaId);
		query.executeUpdate();
	}

	// Exibe Mensagem
	@Transactional(readOnly = true)
	public Mensagem getMessageById(Integer mensagemId) {
		return mensagemRepository.findById(mensagemId)
				.orElseThrow(() -> new RuntimeException("Mensagem não encontrada"));
	}

	// Cria uma nova mensagem - Transactional -> Atomicidade
	@Transactional
	public Mensagem createMessage(Mensagem mensagem) {
		return mensagemRepository.save(mensagem);
	}

	// Atualiza uma mensagem existente
	@Transactional
	public Mensagem updateMessage(Integer mensagemId, Mensagem updatedMensagem) {
		// Verifique se a mensagem existe antes de atualizá-la
		Mensagem existingMensagem = mensagemRepository.findById(mensagemId)
				.orElseThrow(() -> new RuntimeException("Mensagem não encontrada"));
		updatedMensagem.setId(existingMensagem.getId());
		return mensagemRepository.save(updatedMensagem);
	}

	// Exclui uma mensagem
	@Transactional
	public void deleteMessageById(Integer mensagemId) {
		// Verifique se a mensagem existe antes de excluí-la
		Mensagem existingMensagem = mensagemRepository.findById(mensagemId)
				.orElseThrow(() -> new RuntimeException("Mensagem não encontrada"));
		mensagemRepository.delete(existingMensagem);
	}
}
