package io.github.mateus81.mensagensapi.model.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UsuarioDTO {
	
	@NotNull
	private Integer id;
	
	@NotNull
	private String nome;
	
	@NotNull
	private String email;
	
	@NotNull
	@JsonProperty("senha")
	private String senhaNaoProtegida;
	
	// Construtor de teste
	public UsuarioDTO(String nome, String email, String senhaNaoProtegida) {
		this.nome = nome;
		this.email = email;
		this.senhaNaoProtegida = senhaNaoProtegida;
	}
	
	// Construtor padrão
	public UsuarioDTO() {
		
	}
	
	// Construtor de teste de ID
	public UsuarioDTO(Integer id) {
		this.id = id;
	}
	
	// Getters & Setters
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenhaNaoProtegida() {
		return senhaNaoProtegida;
	}

	public void setSenhaNaoProtegida(String senhaNaoProtegida) {
		this.senhaNaoProtegida = senhaNaoProtegida;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	// Tratamento do AssertionFailedError no JUnit
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) {
	        return true;
	    }
	    if (obj == null || getClass() != obj.getClass()) {
	        return false;
	    }

	    UsuarioDTO other = (UsuarioDTO) obj;
	    return Objects.equals(this.nome, other.nome) && 
	    		Objects.equals(this.email, other.email) && Objects.equals(this.id, other.id);
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(this.nome, this.email);
	}
	
}
