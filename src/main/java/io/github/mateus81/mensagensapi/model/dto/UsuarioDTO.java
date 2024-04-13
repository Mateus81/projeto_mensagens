package io.github.mateus81.mensagensapi.model.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UsuarioDTO {
	
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
	
	// Getters & Setters
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
	
}
