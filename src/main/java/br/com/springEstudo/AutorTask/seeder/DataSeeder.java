package br.com.springEstudo.AutorTask.seeder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.springEstudo.AutorTask.infraestructure.entities.TaskEntity;
import br.com.springEstudo.AutorTask.infraestructure.entities.UserEntity;
import br.com.springEstudo.AutorTask.infraestructure.repositories.TaskRepository;
import br.com.springEstudo.AutorTask.infraestructure.repositories.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

	private final UserRepository userRepository;
	private final TaskRepository taskRepository;
	private final Random random = new Random();

	public DataSeeder(UserRepository userRepository, TaskRepository taskRepository) {
		this.userRepository = userRepository;
		this.taskRepository = taskRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		// Popula os dados apenas se o banco de dados estiver vazio
		if (userRepository.count() == 0) {
			seedUsersAndTasks();
		}
	}

	private void seedUsersAndTasks() {
		System.out.println("Iniciando a populacao de dados de teste...");

		// Cria e salva 5 usuarios
		Set<UserEntity> users = new HashSet<>();
		for (int i = 1; i <= 5; i++) {
			UserEntity user = new UserEntity();
			user.setNome("user" + i);
			user.setEmail("user" + i + "@example.com");
			user.setSenha("password" + i); // Senha deve ser criptografada em producao
			users.add(user);
		}
		userRepository.saveAll(users);

		// Para cada usuario, cria um numero aleatorio de tarefas
		for (UserEntity user : users) {
			int numberOfTasks = random.nextInt(5) + 1; // Cria entre 1 e 5 tarefas
			for (int i = 0; i < numberOfTasks; i++) {
				TaskEntity task = new TaskEntity();
				task.setTitulo("Task " + UUID.randomUUID().toString().substring(0, 8));
				task.setDescricao("Descricao para a tarefa " + (i + 1) + " do usuario " + user.getNome());
				task.setCompleted(random.nextBoolean());
				task.setDataConclusao(task.getCompleted() ? LocalDate.now() : null);
				task.setUser(user); // Associa a tarefa ao usuario
				taskRepository.save(task);
			}
		}

		System.out.println("Populacao de dados concluida com sucesso!");
	}
}
