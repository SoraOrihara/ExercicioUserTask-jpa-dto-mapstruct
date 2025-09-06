package br.com.springEstudo.AutorTask.infraestructure.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name="tb_task")
public class TaskEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.UUID)
	private UUID id;
	@Column(nullable = false)
	private String titulo;
	
	private String descricao;
	
	@Column(nullable=false)
	private Boolean completed=false;
	
	@Column(name="data_criacao",nullable = false,updatable = false)
	private LocalDateTime dataCriacao;
	
	private LocalDate dataConclusao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private UserEntity user;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Boolean getCompleted() {
		return completed;
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public LocalDate getDataConclusao() {
		return dataConclusao;
	}

	public void setDataConclusao(LocalDate dataConclusao) {
		this.dataConclusao = dataConclusao;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public UUID getId() {
		return id;
	}

	public TaskEntity() {
		super();
	}

	public TaskEntity(String titulo, String descricao, Boolean completed, LocalDateTime dataCriacao,
			LocalDate dataConclusao, UserEntity user) {
		super();
		this.titulo = titulo;
		this.descricao = descricao;
		this.completed = completed;
		this.dataCriacao = dataCriacao;
		this.dataConclusao = dataConclusao;
		this.user = user;
	}
	
	
	@PrePersist
	public void criadoEm() {
		this.dataCriacao=LocalDateTime.now();
	}
	
	
}
