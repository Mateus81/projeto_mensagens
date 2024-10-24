package io.github.mateus81.mensagensapi.model.entity;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;

// Este é o usuário do aplicativo
@Entity
@JsonView(UsuarioView.Basic.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonInclude(Include.NON_NULL)
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
	private Date dataCadastro;

	@Column(nullable = true)
	@Lob
	private byte[] foto;

	// Um usuário pode ter vários contatos
	@ManyToMany
	@JoinTable(name = "usuario_contatos", joinColumns = @JoinColumn(name = "usuario_id"), 
	inverseJoinColumns = @JoinColumn(name = "contato_id"))
	@JsonIgnore
	private List<Usuario> contatos;
	// E Também conversas!
	@OneToMany(mappedBy = "usuario")
	@JsonIgnore
	private List<Conversa> conversas;
	// Mensagens também...
	@OneToMany(mappedBy = "usuario_remetente")
	@JsonIgnore
	private List<Mensagem> mensagensEnviadas;
	@OneToMany(mappedBy = "usuario_destino")
	@JsonIgnore
	private List<Mensagem> mensagensRecebidas;
	// E arquivos
	@OneToMany(mappedBy = "usuario")
	@JsonIgnore
	private List<Arquivo> arquivos;

	// Construtor
	public Usuario() {
		this.dataCadastro = Date.from(Instant.now());
	}
	
	// Construtor de teste
	public Usuario(String nome, String email) {
		// Validação Explícita
		if(nome == null || nome.trim().isEmpty()) {
			throw new IllegalArgumentException("Nome inválido" + nome);
		}
		
		Pattern pattern = Pattern.compile("^(.+)@(.+)$");
		Matcher matcher = pattern.matcher(email);
		if(!matcher.find()) {
			throw new IllegalArgumentException("Email inválido");
		}
		
		this.nome = nome;
		this.email = email;
	}
	
	// Construtor para teste de ID
	public Usuario(Integer id, String nome) {
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
	
	// Construtor para teste de senha
	public Usuario(Integer id, String nome, String senha) {
		this.id = id;
		this.nome = nome;
		this.senha = senha;
	}
	
	// Construtor para teste de autenticação
	public Usuario(String nome, String email, String senha) {
		this.nome = nome;
		this.email = email;
		this.senha = senha;
	}

	// Construtor para teste integrado
	public Usuario(Integer id) {
		this.id = id;
	}

	// Sobrecarga de método
	@Override
	public int hashCode() {
	    return Objects.hash(id, nome, email);
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
	
	// Insere Senha
	public String getSenha() {
		return senha;
	}
	// Insere a senha
	public void setSenha(String senha) {
		this.senha = senha;
	}

	// Obtém a data de cadastro
	public Date getDataCadastro() {
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
	public List<Usuario> getContatos() {
		return contatos;
	}

	// Insere lista de contatos
	public void setContatos(List<Usuario> contatos) {
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
