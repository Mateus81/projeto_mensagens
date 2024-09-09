package io.github.mateus81.mensagensapi.model.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import io.github.mateus81.mensagensapi.model.entity.Conversa;
import io.github.mateus81.mensagensapi.model.entity.Usuario;

public class MensagemDTO {
	
	@NotNull
	private Integer id;

	@NotNull
	private String texto;
	
	@NotNull
    private Usuario usuarioRemetente;
	
	@NotNull
    private Usuario usuarioDestino;
	
	@NotNull
	private Conversa conversa;
	
    private Boolean vista;
    
    // Construtor padrão 
    public MensagemDTO() {}
    
    // Getters and Setters 
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Usuario getUsuarioRemetente() {
		return usuarioRemetente;
	}

	public void setUsuarioRemetente(Usuario UsuarioRemetente) {
		this.usuarioRemetente = UsuarioRemetente;
	}

	public Usuario getUsuarioDestino() {
		return usuarioDestino;
	}

	public void setUsuarioDestino(Usuario usuarioDestino) {
		this.usuarioDestino = usuarioDestino;
	}

	public Conversa getConversa() {
		return conversa;
	}
	
	public void setConversa(Conversa conversa) {
		this.conversa = conversa;
	}
	
	public Boolean getVista() {
		return vista;
	}

	public void setVista(Boolean vista) {
		this.vista = vista;
	}
	
	// Tratamento de erro
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    MensagemDTO that = (MensagemDTO) o;
	    return Objects.equals(id, that.id) &&
	           Objects.equals(texto, that.texto) &&
	           Objects.equals(usuarioDestino, that.usuarioDestino) &&
	    	   Objects.equals(usuarioRemetente, that.usuarioRemetente) &&
	    	   Objects.equals(conversa, that.conversa) &&
	    	   Objects.equals(vista, that.vista);
	}

	@Override
	public int hashCode() {
	    return Objects.hash(id, texto, usuarioDestino, usuarioRemetente, conversa, vista);
	}
}
