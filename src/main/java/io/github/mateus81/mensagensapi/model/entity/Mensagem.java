package io.github.mateus81.mensagensapi.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


// Esta é a Mensagem que estará presente na conversa dos usuários.
@Entity
public class Mensagem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Lob
	private String texto;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataHoraEnvio;

	// Objetos envolvidos
	@ManyToOne
	private Usuario usuario_remetente;

	@ManyToOne
	private Usuario usuario_destino;

	@Column
	private Boolean vista;

	// Construtor padrão
	public Mensagem() {

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
		this.dataHoraEnvio = dataHoraEnvio;
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

}
