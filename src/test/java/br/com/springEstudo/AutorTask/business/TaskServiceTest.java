package br.com.springEstudo.AutorTask.business;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
	@DisplayName("Should save task and return taskResponseDto")
	void testCreateTask() {
		//Preparação
		UUID userId= UUID.randomUUID();
		UserEntity user = new UserEntity(userId, "Caio", "caio@gmail", "1234");
		TaskRequestDto request = new TaskRequestDto("tarefa", "descrição da tarefa", LocalDate.of(2025, 11, 02));
		TaskEntity task = new TaskEntity(request.titulo(), request.descricao(),false,LocalDateTime.now(),request.dataConclusao());
		TaskEntity taskSalva = new TaskEntity(UUID.randomUUID(), task.getTitulo(),task.getDescricao(),task.getCompleted(),task.getDataCriacao(),task.getDataConclusao(), user);
		TaskResponseDto response= new TaskResponseDto(taskSalva.getId(), taskSalva.getTitulo(), taskSalva.getDescricao(), taskSalva.getCompleted(), taskSalva.getDataConclusao(), taskSalva.getUser().getId());
		
		//Configuração dos mocks
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(taskMapper.paraTaskEntity(request)).thenReturn(task);
		when(taskRepository.save(any(TaskEntity.class))).thenReturn(taskSalva);
		when(taskMapper.paraTaskResponse(any(TaskEntity.class))).thenReturn(response);
		
		//Ação 
		TaskResponseDto resultado =taskService.createTask(userId, request);
		
		//Verificação
		verify(userRepository).findById(userId);
		verify(taskMapper).paraTaskEntity(request);
		verify(taskRepository).save(any(TaskEntity.class));
		verify(taskMapper).paraTaskResponse(any(TaskEntity.class));
		
		//Asserts
		assertNotNull(resultado);
		assertEquals(response.id(), resultado.id());
		assertEquals(response.titulo(),resultado.titulo());
		assertEquals(response.userId(),resultado.userId());
		
	}
	
	@Test
	@DisplayName("Should throw a ResourceNotFound userId")
	void testCreateTaskCase2() {
		//Preparação
		UUID userId= UUID.randomUUID();
		TaskRequestDto request = new TaskRequestDto("tarefa", "descrição da tarefa", LocalDate.of(2025, 11, 02));
		when(userRepository.findById(userId)).thenReturn(Optional.empty());
		//Ação
		assertThrows(ResourceNotFoundException.class, ()-> taskService.createTask(userId, request));
		
		//Verificação
		verify(userRepository).findById(userId);
	}
	@Test
	void testFindTaskById() {
		fail("Not yet implemented");
	}

	@Test
	void testFindByUserId() {
		fail("Not yet implemented");
	}

	@Test
	void testFindAll() {
		fail("Not yet implemented");
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
	void testDeleteById() {
		fail("Not yet implemented");
	}

}
