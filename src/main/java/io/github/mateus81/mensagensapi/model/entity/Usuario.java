package io.github.mateus81.mensagensapi.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;

// Este é o usuário do aplicativo
@Entity
public class Usuario {
	// ID auto-incrementado não precisará do setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	@Column(nullable = false)
	private String nome;

	@NotNull
	@Column(unique = true, nullable = false)
	@Email
	private String email;

	@NotNull
	@Column(nullable = false)
	private String senha;

	@NotNull
	@Column(nullable = false, updatable = false)
	private LocalDateTime dataCadastro;

	@Column(nullable = true)
	@Lob
	private byte[] foto;

	// Um usuário pode ter vários contatos
	@OneToMany(mappedBy = "usuario")
	private List<Contato> contatos;
	// E Também conversas!
	@OneToMany(mappedBy = "usuario")
	private List<Conversa> conversas;
	// Mensagens também...
	@OneToMany(mappedBy = "usuario_remetente")
	private List<Mensagem> mensagensEnviadas;
	@OneToMany(mappedBy = "usuario_destino")
	private List<Mensagem> mensagensRecebidas;
	// E arquivos
	@OneToMany(mappedBy = "usuario")
	private List<Arquivo> arquivos;

	// Construtor
	public Usuario() {
		this.dataCadastro = LocalDateTime.now();
	}

	// Obtém o ID
	public Integer getId() {
		return id;
	}

	// Insere o ID, se necessário
	public void setId(Integer id) {
		this.id = id;
	}

	// Obtém o nome
	public String getNome() {
		return nome;
	}

	// Cria o nome do usuário
	public void setNome(String nome) {
		this.nome = nome;
	}

	// Obtém o email
	public String getEmail() {
		return email;
	}

	// Insere o email
	public void setEmail(String email) {
		this.email = email;
	}

	// Autowired para injeção de dependencia
	@Autowired
	private PasswordEncoder passwordEncoder;

	// Cria a senha
	public void setSenha(String senha) {
		this.senha = encodePassword(senha);
	}

	// Protege e valida a senha
	private String encodePassword(String senha) {
		if (passwordEncoder != null && senha != null && !senha.isEmpty()) {
			return passwordEncoder.encode(senha);
		}
		return senha;
	}

	// Obtém a data de cadastro
	public LocalDateTime getDataCadastro() {
		return dataCadastro;
	}

	// Obtém a foto
	public byte[] getfoto() {
		return foto;
	}

	// Insere a foto
	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	// Obtém a lista de contatos
	public List<Contato> getContatos() {
		return contatos;
	}

	// Insere lista de contatos
	public void setContatos(List<Contato> contatos) {
		this.contatos = contatos;
	}

	// Obtém as conversas
	public List<Conversa> getConversas() {
		return conversas;
	}

	// Insere as conversas
	public void setConversas(List<Conversa> conversas) {
		this.conversas = conversas;
	}

	// Obtém as mensagens enviadas
	public List<Mensagem> getMensagensEnviadas() {
		return mensagensEnviadas;
	}

	// Insere mensagens enviadas
	public void setMensagensEnviadas(List<Mensagem> mensagensEnviadas) {
		this.mensagensEnviadas = mensagensEnviadas;
	}

	// Obtém mensagens recebidas
	public List<Mensagem> getMensagensRecebidas() {
		return mensagensRecebidas;
	}

	// Insere mensagens recebidas
	public void setMensagensRecebidas(List<Mensagem> mensagensRecebidas) {
		this.mensagensRecebidas = mensagensRecebidas;
	}

	// Obtém lista de arquivos
	public List<Arquivo> getArquivos() {
		return arquivos;
	}

	// Insere arquivos
	public void setArquivos(List<Arquivo> arquivos) {
		this.arquivos = arquivos;

	}
}
