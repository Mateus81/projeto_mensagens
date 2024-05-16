package io.github.mateus81.mensagensapi.model.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import io.github.mateus81.mensagensapi.model.entity.Usuario;

public class ConversaDTO {

	@NotNull
	private Integer id;
	
	@NotNull
	private Usuario usuario;
	
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
		
	// Tratamento do erro AssertionFailed
		@Override
		public boolean equals(Object o) {
		    if (this == o) return true;
		    if (o == null || getClass() != o.getClass()) return false;
		    ConversaDTO that = (ConversaDTO) o;
		    return Objects.equals(id, that.id) &&
		           Objects.equals(usuario, that.usuario);
		}

		@Override
		public int hashCode() {
		    return Objects.hash(id, usuario);
		}
}
