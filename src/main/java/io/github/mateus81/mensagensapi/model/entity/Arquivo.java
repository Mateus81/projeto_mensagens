package io.github.mateus81.mensagensapi.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


// Este é o arquivo que o usuário pode enviar
@Entity
public class Arquivo {
	
	// Atributos
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = false)
	private String tipo;

	@Lob
	private byte[] conteudo;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataEnvio;

	// Objeto que envia o arquivo
	@ManyToOne
	@JoinColumn(name = "usuarioId", nullable = false)
	private Usuario usuario;
	
	// Construtor padrão
	public Arquivo() {
		this.dataEnvio = new Date();
	}
	
	// Construtor de teste simples
	public Arquivo(Integer id) {
		this.id = id;
	}
	
	//Construtor de teste
	public Arquivo(Integer id, String nome, String tipo, byte[] conteudo) {
        // Validações IF Statement
	       if(id <= 0) {
	           throw new IllegalArgumentException("Id inválido" + id);
	       }

	       if(nome == null || nome.trim().isEmpty()) {
	           throw new IllegalArgumentException("Nome inválido" + nome);
	       }            

	       if(tipo == null || tipo.trim().isEmpty()) {
	           throw new IllegalArgumentException("Tipo de arquivo inválido" + tipo);
           }
	       
	       if(conteudo == null) {
	           throw new IllegalArgumentException("Conteúdo do arquivo inválido" + conteudo);
	       }

	       this.id = id;            
	       this.nome = nome;
	       this.tipo = tipo;
	       this.conteudo = conteudo;
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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Long getTamanho() {
		return (long) conteudo.length;
	}

	public Date getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public byte[] getConteudo() {
		return conteudo;
	}

	public void setConteudo(byte[] conteudo) {
		this.conteudo = conteudo;
	}
}
