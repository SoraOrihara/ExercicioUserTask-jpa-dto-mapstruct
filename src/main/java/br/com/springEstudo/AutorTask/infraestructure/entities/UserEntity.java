package br.com.springEstudo.AutorTask.infraestructure.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="tb_user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.UUID)
	private UUID id;
	
	@Column(name="nome",unique = true,nullable = false)
	private String nome;
	@Column(name="email",unique = true,nullable = false)
	private String email;
	@Column(name="senha",nullable = false)
	private String senha;
	
	@OneToMany(mappedBy = "user",cascade=CascadeType.ALL, orphanRemoval = true)
	private Set<TaskEntity> tasks = new HashSet<>();

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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Set<TaskEntity> getTasks() {
		return tasks;
	}

	public void setTasks(Set<TaskEntity> tasks) {
		this.tasks = tasks;
	}

	public UUID getId() {
		return id;
	}

	public UserEntity() {
		super();
	}

	public UserEntity(String nome, String email, String senha, Set<TaskEntity> tasks) {
		super();
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.tasks = tasks;
	}
	
	
}
