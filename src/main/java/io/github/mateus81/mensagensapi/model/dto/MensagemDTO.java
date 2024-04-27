package io.github.mateus81.mensagensapi.model.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;

public class MensagemDTO {
	
	@NotNull
	private Integer id;

	@NotNull
	private String texto;
	
	@NotNull
    private Integer idUsuarioRemetente;
	
	@NotNull
    private Integer idUsuarioDestino;
	
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

	public Integer getIdUsuarioRemetente() {
		return idUsuarioRemetente;
	}

	public void setIdUsuarioRemetente(Integer idUsuarioRemetente) {
		this.idUsuarioRemetente = idUsuarioRemetente;
	}

	public Integer getIdUsuarioDestino() {
		return idUsuarioDestino;
	}

	public void setIdUsuarioDestino(Integer idUsuarioDestino) {
		this.idUsuarioDestino = idUsuarioDestino;
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
	           Objects.equals(idUsuarioDestino, that.idUsuarioDestino) &&
	    	   Objects.equals(idUsuarioRemetente, that.idUsuarioRemetente) &&
	    	   Objects.equals(vista, that.vista);
	}

	@Override
	public int hashCode() {
	    return Objects.hash(id, texto, idUsuarioDestino, idUsuarioRemetente, vista);
	}
}
