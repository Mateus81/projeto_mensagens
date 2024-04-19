package io.github.mateus81.mensagensapi.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


// Esta é a Mensagem que estará presente na conversa dos usuários.
@Entity
@JsonInclude(Include.NON_NULL)
public class Mensagem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Lob
	private String texto;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataHoraEnvio;

	// Objetos envolvidos
	@ManyToOne
	private Usuario usuario_remetente;

	@ManyToOne
	@JoinColumn(name = "usuario_destino")
	private Usuario usuario_destino;

	@Column
	private Boolean vista;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "conversaId")
	private Conversa conversa;

	// Construtor padrão
	public Mensagem() {}
	
	// Construtor teste integrado
	public Mensagem(Date dataHoraEnvio) {
		this.dataHoraEnvio = (dataHoraEnvio != null) ? dataHoraEnvio : new Date();
	}
	
	// Construtor de teste unitário
	public Mensagem(Integer id, String texto, Boolean vista) {
		if(id <= 0) {
			throw new IllegalArgumentException("Id inválido");
		}
		if(texto == null | texto.trim().isEmpty()) {
			throw new IllegalArgumentException("Texto inválido");
		}
		
		this.id = id;
		this.texto = texto;
		this.vista = false;
	}

	// Getters & Setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Date getDataHoraEnvio() {
		return dataHoraEnvio;
	}

	public void setData_hora_envio(Date dataHoraEnvio) {
		this.dataHoraEnvio = (dataHoraEnvio != null) ? dataHoraEnvio : new Date();
	}

	public Usuario getUsuarioremetente() {
		return usuario_remetente;
	}

	public void setUsuarioremetente(Usuario usuario_remetente) {
		this.usuario_remetente = usuario_remetente;
	}

	public Usuario getUsuariodestino() {
		return usuario_destino;
	}

	public void setUsuariodestino(Usuario usuario_destino) {
		this.usuario_destino = usuario_destino;
	}

	public boolean isVista() {
		return vista;
	}

	public void setVista(Boolean vista) {
		this.vista = vista;
	}
	
	public Conversa getConversa() {
		return conversa;
	}

	public void setConversa(Conversa conversa) {
		this.conversa = conversa;
	}

}
