package io.github.mateus81.mensagensapi.model.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import io.github.mateus81.mensagensapi.model.service.ConversaService.StatusConversa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

// Esta é a conversa entre os usuários.
@Entity
public class Conversa {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private LocalDateTime data_inicio;
	
	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime data_termino;
	
	// Objeto Usuário
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;
	
	// Status
	@Column
	private StatusConversa status;
	
	// Construtor
	public Conversa(Usuario usuario) {
		this.usuario = usuario;
		this.data_inicio = null;
		
	}
	// Getters & Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getData_inicio() {
		return data_inicio;
	}

	public void setData_inicio(LocalDateTime data_inicio) {
		this.data_inicio = data_inicio;
	}

	public LocalDateTime getData_termino() {
		return data_termino;
	}

	public void setData_termino(LocalDateTime data_termino) {
		this.data_termino = data_termino;
	}
	
	public StatusConversa getStatus() {
		return status;
	}
	
	public void setStatus(StatusConversa status) {
		this.status = status;
	}

}
