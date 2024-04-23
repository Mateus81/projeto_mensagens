package io.github.mateus81.mensagensapi.model.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;

public class ConversaDTO {

	@NotNull
	private Integer id;
	
	@NotNull
	private Integer usuarioId;
	
	// Construtor padrão
	public ConversaDTO() {}
	
	// Getters and Setters
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getUsuarioId() {
		return usuarioId;
	}
	
	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}
		
	// Tratamento do erro AssertionFailed
		@Override
		public boolean equals(Object o) {
		    if (this == o) return true;
		    if (o == null || getClass() != o.getClass()) return false;
		    ConversaDTO that = (ConversaDTO) o;
		    return Objects.equals(id, that.id) &&
		           Objects.equals(usuarioId, that.usuarioId);
		}

		@Override
		public int hashCode() {
		    return Objects.hash(id, usuarioId);
		}
}
