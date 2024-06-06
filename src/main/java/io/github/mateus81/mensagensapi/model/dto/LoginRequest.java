package io.github.mateus81.mensagensapi.model.dto;

import javax.validation.constraints.NotNull;

public class LoginRequest {
	// Atributos
	@NotNull
	private String email;
	@NotNull
	private String senha;
	
	// Getters and Setters
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
}
