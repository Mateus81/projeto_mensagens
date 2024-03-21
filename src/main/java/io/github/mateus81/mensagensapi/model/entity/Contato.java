package io.github.mateus81.mensagensapi.model.entity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

// Este é o contato que conversa com o Usuário
@Entity
public class Contato {
	// Campo auto-incremento
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	
	@NotNull
	@Column(nullable = false)
	public String nome;
	
	@NotNull
	@Column(unique = true, nullable = false)
	@Email
	public String email;
	
	@NotNull
	@Column(nullable = false, unique = true)
	public String telefone;
	
	// Objeto Usuário
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;
	
	@Column(nullable = true)
	@Lob
	private byte[] foto;
	
	// Construtor padrão
	public Contato() {
		
	}
	// Getters & Setters
	public Long getId(){
		return id;
	}
	
	public void setId(Long id) {
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
	
	public byte[] getfoto(){
		return foto;
	}
	
	public void setFoto(byte[] foto) {
		this.foto = foto;
	}
}
