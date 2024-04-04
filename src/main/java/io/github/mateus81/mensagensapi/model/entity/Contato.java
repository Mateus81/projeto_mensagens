package io.github.mateus81.mensagensapi.model.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


// Este é o contato que conversa com o Usuário
@Entity
public class Contato {
	// Campo auto-incremento
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

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
	@JoinColumn(name = "usuarioId", nullable = false)
	private Usuario usuario;

	@Column(nullable = true)
	@Lob
	private byte[] foto;

	// Construtor padrão
	public Contato() {}
	
	// Construtor de Teste
	public Contato(String nome, String telefone) {
		// IF Statement
		if(nome == null || nome.trim().isEmpty()) {
			throw new IllegalArgumentException("Nome inválido");
		}
		if(telefone == null || telefone.trim().isEmpty()) {
			throw new IllegalArgumentException("Telefone inválido");
		}
		
		this.nome = nome;
		this.telefone = telefone;
	}
	
	// Construtor para teste de ID
		public Contato(Integer id, String nome) {
			// Validação Explícita
			if(id <= 0) {
				throw new IllegalArgumentException("ID inválido");
			}
			if(nome == null || nome.trim().isEmpty()) {
				throw new IllegalArgumentException("Nome inválido" + nome);
			}
			
			this.id = id;
			this.nome = nome;
		}
	
	// Sobrecarga
	public int hashCode() {
		return Objects.hash(nome, email);
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

	public byte[] getfoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}
}
