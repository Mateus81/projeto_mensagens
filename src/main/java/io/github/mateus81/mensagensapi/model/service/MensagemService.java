package io.github.mateus81.mensagensapi.model.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.mateus81.mensagensapi.model.dto.MensagemDTO;
import io.github.mateus81.mensagensapi.model.entity.Conversa;
import io.github.mateus81.mensagensapi.model.entity.Mensagem;
import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.repository.ConversaRepository;
import io.github.mateus81.mensagensapi.model.repository.MensagemRepository;
import io.github.mateus81.mensagensapi.model.repository.UsuarioRepository;

@Service
public class MensagemService {

	private final MensagemRepository mensagemRepository;
	private final UsuarioRepository usuarioRepository;
	private final ConversaRepository conversaRepository;
	
	@Autowired
	private EntityManager entityManager;

	// Construtor
	public MensagemService(MensagemRepository mensagemRepository, UsuarioRepository usuarioRepository, 
			ConversaRepository conversaRepository) {
		this.mensagemRepository = mensagemRepository;
		this.usuarioRepository = usuarioRepository;
		this.conversaRepository = conversaRepository;
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
	
	// Exibe mensagem pelo ID da conversa
	@Transactional(readOnly = true)
	public List<Mensagem> getMensagensByConversaId(Integer conversaId){
		List<Mensagem> mensagens = mensagemRepository.findByConversaId(conversaId);
		return mensagens;
	}

	// Cria uma nova mensagem - Transactional -> Atomicidade
	@Transactional
	public Mensagem createMessage(Integer conversaId, MensagemDTO dto) throws Exception {
		Usuario remetente = usuarioRepository.findByEmail(dto.getUsuarioRemetente().getEmail());
		if(remetente == null) {
			throw new Exception("Remetente não encontrado");
		}
		Conversa conversa = conversaRepository.findById(conversaId).orElseThrow((
				) -> new Exception("Conversa não encontrada."));
		Usuario destinatario = conversa.getUsuarioDest();
		
		Mensagem mensagem = new Mensagem();
		mensagem.setId(dto.getId());
		mensagem.setData_hora_envio(new Date());
		mensagem.setTexto(dto.getTexto());
		mensagem.setUsuarioremetente(remetente);
		mensagem.setUsuariodestino(destinatario);
		mensagem.setConversa(conversa);
		mensagem.setVista(false);
		System.out.println("Salvando mensagem" + mensagem);
		return mensagemRepository.save(mensagem);
	}

	// Atualiza uma mensagem existente
	@Transactional
	public Mensagem updateMessage(Integer mensagemId, Mensagem updatedMensagem) {
		// Verifique se a mensagem existe antes de atualizá-la
		Mensagem existingMensagem = mensagemRepository.findById(mensagemId)
				.orElseThrow(() -> new RuntimeException("Mensagem não encontrada"));
		existingMensagem.setTexto(updatedMensagem.getTexto());
		return mensagemRepository.save(existingMensagem);
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
