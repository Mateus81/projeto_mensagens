package io.github.mateus81.mensagensapi.model.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import io.github.mateus81.mensagensapi.model.service.ConversaService.StatusConversa;

// Esta é a conversa entre os usuários.
@Entity
public class Conversa {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date data_inicio;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date data_termino;

	// Objeto Usuário
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuarioId", nullable = false)
	private Usuario usuario;
	
	// Objeto Mensagens
	@OneToMany(mappedBy = "conversa", cascade = CascadeType.ALL)
	private List<Mensagem> mensagens;

	// Status
	@Column
	private StatusConversa status;

	// Construtor
	public Conversa(Usuario usuario) {
		this.usuario = usuario;
		this.data_inicio = null;

	}
	
	// Construtor padrão
	public Conversa() {
		
	}

	// Getters & Setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getData_inicio() {
		return data_inicio;
	}

	public void setData_inicio(Date data_inicio) {
		this.data_inicio = data_inicio;
	}

	public Date getData_termino() {
		return data_termino;
	}

	public void setData_termino(Date data_termino) {
		this.data_termino = data_termino;
	}

	public StatusConversa getStatus() {
		return status;
	}

	public void setStatus(StatusConversa status) {
		this.status = status;
	}
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Mensagem> getMensagens() {
		return mensagens;
	}

	public void setMensagens(List<Mensagem> mensagens) {
		this.mensagens = mensagens;
	}

}
