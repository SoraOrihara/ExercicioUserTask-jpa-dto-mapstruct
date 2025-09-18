package br.com.springEstudo.AutorTask.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.springEstudo.AutorTask.business.dto.TaskRequestDto;
import br.com.springEstudo.AutorTask.business.dto.TaskResponseDto;
import br.com.springEstudo.AutorTask.business.dto.TaskResponseListUserDto;
import br.com.springEstudo.AutorTask.business.dto.UserReferenceDto;
import br.com.springEstudo.AutorTask.business.mapstruct.TaskMapper;
import br.com.springEstudo.AutorTask.exceptions.ResourceNotFoundException;
import br.com.springEstudo.AutorTask.infraestructure.entities.TaskEntity;
import br.com.springEstudo.AutorTask.infraestructure.entities.UserEntity;
import br.com.springEstudo.AutorTask.infraestructure.repositories.TaskRepository;
import br.com.springEstudo.AutorTask.infraestructure.repositories.UserRepository;

class TaskServiceTest {

	@Mock
	private TaskRepository taskRepository;

	@Mock
	private TaskMapper taskMapper;

	@Mock
	private UserRepository userRepository;

	@Autowired
	@InjectMocks
	private TaskService taskService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("Should  create and save a task and return taskResponseDto")
	void testCreateTask() {
		// Preparação
		UUID userId = UUID.randomUUID();
		UserEntity user = new UserEntity(userId, "Caio", "caio@gmail", "1234");
		TaskRequestDto request = new TaskRequestDto("tarefa", "descrição da tarefa", LocalDate.of(2025, 11, 02));
		
		TaskEntity task = new TaskEntity(request.titulo(), request.descricao(), false, LocalDateTime.now(),
				request.dataConclusao());
		
		TaskEntity taskSalva = new TaskEntity(UUID.randomUUID(), task.getTitulo(), task.getDescricao(),
				task.getCompleted(), task.getDataCriacao(), task.getDataConclusao(), user);
		
		TaskResponseDto response = new TaskResponseDto(taskSalva.getId(), taskSalva.getTitulo(),
				taskSalva.getDescricao(), taskSalva.getCompleted(), taskSalva.getDataConclusao(),
				taskSalva.getUser().getId());

		// Configuração dos mocks
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(taskMapper.paraTaskEntity(request)).thenReturn(task);
		when(taskRepository.save(any(TaskEntity.class))).thenReturn(taskSalva);
		when(taskMapper.paraTaskResponse(any(TaskEntity.class))).thenReturn(response);

		// Ação
		TaskResponseDto resultado = taskService.createTask(userId, request);

		// Verificação
		verify(userRepository).findById(userId);
		verify(taskMapper).paraTaskEntity(request);
		verify(taskRepository).save(any(TaskEntity.class));
		verify(taskMapper).paraTaskResponse(any(TaskEntity.class));

		// Asserts
		assertNotNull(resultado);
		assertEquals(response.id(), resultado.id());
		assertEquals(response.titulo(), resultado.titulo());
		assertEquals(response.userId(), resultado.userId());

	}

	@Test
	@DisplayName("Should throw a ResourceNotFound userId")
	void testCreateTaskCase2() {
		// Preparação
		UUID userId = UUID.randomUUID();
		TaskRequestDto request = new TaskRequestDto("tarefa", "descrição da tarefa", LocalDate.of(2025, 11, 02));
		when(userRepository.findById(userId)).thenReturn(Optional.empty());
		// Ação
		assertThrows(ResourceNotFoundException.class, () -> taskService.createTask(userId, request));

		// Verificação
		verify(userRepository).findById(userId);
	}

	@Test
	@DisplayName("Should find a task By id and return a TaskResponseDto")
	void testFindTaskById() {
		//Preparação
		UUID idTask= UUID.randomUUID();
		UserEntity user = new UserEntity(UUID.randomUUID(), "Caio", "caio@gmail", "1234");
		TaskEntity task = new TaskEntity("Comprar pão", "Padaria", false, LocalDateTime.now(), LocalDate.of(2025, 11, 20),user);
		TaskResponseDto response= new TaskResponseDto(UUID.randomUUID(),"Comprar pão", "Padaria", false, LocalDate.of(2025, 11, 20),user.getId());
		
		//Condiguração dos mocks
		when(taskRepository.findById(idTask)).thenReturn(Optional.of(task));
		when(taskMapper.paraTaskResponse(task)).thenReturn(response);
		
		//Ação
		TaskResponseDto result =taskService.findTaskById(idTask);
		
		//Verificação
		verify(taskRepository).findById(idTask);
		verify(taskMapper).paraTaskResponse(task);
		
		//Asserts
		assertNotNull(result);
		assertEquals(result.id(),response.id());
		assertEquals(result.userId(),response.userId());
		assertEquals(result.dataConclusao(),response.dataConclusao());
		
	}
	@Test
	@DisplayName("Should not find a task and Throw a ResourceNotFoundException")
	void testFindTaskByIdCase2() {
		//Preparação
		UUID idTask= UUID.randomUUID();
		
		when(taskRepository.findById(idTask)).thenReturn(Optional.empty());
		
		assertThrows(ResourceNotFoundException.class,()->taskService.findTaskById(idTask));
		
		verify(taskRepository).findById(idTask);
		
	}

	@Test
	@DisplayName("FindByUserId Should return a List<TaskResponseListUserDto> ")
	void testFindByUserId() {
		// Preparacao
		UUID userId = UUID.randomUUID();
		UserEntity user = new UserEntity(userId, "Caio", "caio@gmail", "1234");
		UserReferenceDto userReference = new UserReferenceDto(userId,"Caio","caio@gmail");
		// Lista das tasks
		List<TaskEntity> tasks = List
				.of(new TaskEntity("Comprar pão", "Padaria", false, LocalDateTime.now(), LocalDate.of(2025, 11, 20)));

		// Lista de taskResponse
		List<TaskResponseListUserDto> tasksResponse = List.of(new TaskResponseListUserDto(UUID.randomUUID(),
				"Comprar pão", "Padaria", false, LocalDate.of(2025, 11, 20), userReference));

		// Preparação dos mocks
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(taskRepository.findByUser_id(userId)).thenReturn(tasks);
		when(taskMapper.paraTaskResponseListUser(tasks)).thenReturn(tasksResponse);

		// Ação
		List<TaskResponseListUserDto> lista = taskService.findByUserId(userId);

		// Verificação
		verify(userRepository).findById(userId);
		verify(taskRepository).findByUser_id(userId);
		verify(taskMapper).paraTaskResponseListUser(tasks);

		// Asserts
		assertNotNull(lista);
		assertEquals(1, lista.size());
		assertEquals(lista.get(0).id(), tasksResponse.get(0).id());
		assertEquals(lista.get(0).user(), tasksResponse.get(0).user());
	}

	@Test
	@DisplayName("FindAll should return a task listResponseDto")
	void testFindAll() {
		//Preparação
		UserEntity user = new UserEntity(UUID.randomUUID(), "Caio", "caio@gmail", "1234");
		 // Simular a lista que o repositório retornaria
	    List<TaskEntity> tasksFromRepo = List.of(new TaskEntity("Comprar pão", "Padaria", false, LocalDateTime.now(), LocalDate.of(2025, 11, 20), user));
		List<TaskResponseDto>listResponseDto= List.of(new TaskResponseDto(UUID.randomUUID(),
				"Comprar pão", "Padaria", false, LocalDate.of(2025, 11, 20), user.getId()));
		
		when(taskRepository.findAll()).thenReturn(tasksFromRepo);
		when(taskMapper.paraListTaskResponse(tasksFromRepo)).thenReturn(listResponseDto);
		
		//Ação
		List<TaskResponseDto>resultado = taskService.findAll();
		
		//Verificação
		verify(taskRepository).findAll();
		verify(taskMapper).paraListTaskResponse(tasksFromRepo);
		//Asserts
		assertNotNull(resultado);
		assertEquals(1,resultado.size());
		assertEquals(resultado.get(0).id(),listResponseDto.get(0).id());
		assertEquals(resultado.get(0).userId(),listResponseDto.get(0).userId());
		
	}

	@Test
	void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	void testCompleteTask() {
		fail("Not yet implemented");
	}

	@Test
	@DisplayName("DeleteById Should delete A task")
	void testDeleteById() {
		//Preparação
		UUID id = UUID.randomUUID();
		when(taskRepository.existsById(id)).thenReturn(true);
		
		//Ação
		taskService.deleteById(id);
		
		//Verificação
		verify(taskRepository).existsById(id);
		verify(taskRepository).deleteById(id);
		
		
	}
	@Test
	@DisplayName("DeleteByID Should throw ResourceNotFundException")
	void testDeleteByIdCase2() {
		//Preparação
		UUID id = UUID.randomUUID();
		when(taskRepository.existsById(id)).thenReturn(false);
		
		//Ação
		assertThrows(ResourceNotFoundException.class,()->taskService.deleteById(id));
		
		//Verificação
		verify(taskRepository).existsById(id);
		verify(taskRepository,never()).deleteById(id);
		
	}

}
