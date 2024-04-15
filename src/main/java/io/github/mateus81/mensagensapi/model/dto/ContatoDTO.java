package io.github.mateus81.mensagensapi.model.dto;

import javax.validation.constraints.NotNull;

public class ContatoDTO {

	@NotNull
	private String nome;
	
	@NotNull
	private String email;
	
	@NotNull
	private String telefone;
	
	@NotNull
	private Integer usuario;
	
	// Construtor
	public ContatoDTO(String nome, String email, String telefone, Integer usuario) {
		this.nome = nome;
		this.email = email;
		this.telefone = telefone;
		this.usuario = usuario;
	}

	// Construtor padrão
	public ContatoDTO() {}

	// Getters and Setters
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Integer getUsuario() {
		return usuario;
	}

	public void setUsuario(Integer usuario) {
		this.usuario = usuario;
	}
	
	
}
