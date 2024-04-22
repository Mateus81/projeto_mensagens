package io.github.mateus81.mensagensapi.model.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.github.mateus81.mensagensapi.model.entity.Usuario;

public class ContatoDTO {
	
	@NotNull
	private Integer id;
	
	@NotNull
	private String nome;
	
	@NotNull
	private String email;
	
	@NotNull
	private String telefone;
	
	@NotNull
	@JsonIgnore
	private Usuario usuario;
	
	// Construtor
	public ContatoDTO(String nome, String email, String telefone, Usuario usuario) {
		this.nome = nome;
		this.email = email;
		this.telefone = telefone;
		this.usuario = usuario;
	}

	// Construtor padrão
	public ContatoDTO() {}
	
	// Construtor de teste de ID
	public ContatoDTO(Integer id) {
		this.id = id;
	}

	// Getters and Setters
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
	    ContatoDTO that = (ContatoDTO) o;
	    return Objects.equals(id, that.id) &&
	           Objects.equals(nome, that.nome) && 
	           Objects.equals(email, that.email) && 
	           Objects.equals(telefone, that.telefone) && 
	           Objects.equals(usuario, that.usuario);
	}

	@Override
	public int hashCode() {
	    return Objects.hash(id, nome, email, telefone, usuario);
	}
	
}
