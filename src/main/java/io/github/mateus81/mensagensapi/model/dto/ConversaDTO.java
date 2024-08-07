package io.github.mateus81.mensagensapi.model.dto;

import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import io.github.mateus81.mensagensapi.model.entity.Usuario;

public class ConversaDTO {

	@NotNull
	private Integer id;
	
	@NotNull
	private Usuario usuario;
	
	@NotNull
	private Usuario usuarioDest;

	private List<MensagemDTO> mensagens;
	
	// Construtor padrão
	public ConversaDTO() {}
	
	// Getters and Setters
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Usuario getUsuarioDest() {
		return usuarioDest;
	}	
	
	public void setUsuarioDest(Usuario usuarioDest) {
		this.usuarioDest = usuarioDest;
	}
	
	public List<MensagemDTO> getMensagens(){
		return mensagens;
	}
	
	public void setMensagens(List<MensagemDTO> mensagens) {
		this.mensagens = mensagens;
	}
	
	// Tratamento do erro AssertionFailed
		@Override
		public boolean equals(Object o) {
		    if (this == o) return true;
		    if (o == null || getClass() != o.getClass()) return false;
		    ConversaDTO that = (ConversaDTO) o;
		    return Objects.equals(id, that.id) &&
		           Objects.equals(usuario, that.usuario) &&
		           Objects.equals(usuarioDest, that.usuarioDest) &&
		           Objects.equals(mensagens, that.mensagens);
		}

		@Override
		public int hashCode() {
		    return Objects.hash(id, usuario, usuarioDest, mensagens);
		}
}
