package br.com.springEstudo.AutorTask.business;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.springEstudo.AutorTask.business.dto.TaskRequestDto;
import br.com.springEstudo.AutorTask.business.dto.TaskResponseDto;
import br.com.springEstudo.AutorTask.business.dto.TaskResponseListUserDto;
import br.com.springEstudo.AutorTask.business.mapstruct.TaskMapper;
import br.com.springEstudo.AutorTask.exceptions.ResourceNotFoundException;
import br.com.springEstudo.AutorTask.infraestructure.entities.TaskEntity;
import br.com.springEstudo.AutorTask.infraestructure.entities.UserEntity;
import br.com.springEstudo.AutorTask.infraestructure.repositories.TaskRepository;
import br.com.springEstudo.AutorTask.infraestructure.repositories.UserRepository;

@Service
public class TaskService {

	private final TaskRepository taskRepository;
	private final TaskMapper taskMapper;
	private final UserRepository userRepository;
	
	public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, UserRepository userRepository) {
		this.taskRepository=taskRepository;
		this.taskMapper=taskMapper;
		this.userRepository=userRepository;
	}
	
	public TaskResponseDto createTask(UUID userId, TaskRequestDto request) {
		UserEntity user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("Usuario com id: "+userId+" não encontrado"));
		TaskEntity task =taskMapper.paraTaskEntity(request);
		task.setUser(user);
		TaskEntity taskSalva = taskRepository.save(task);
		return taskMapper.paraTaskResponse(taskSalva);
	}
	
	public TaskResponseDto findTaskById(UUID id) {
		TaskEntity task = taskRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Task com id: "+id+" não encontrado"));
		return taskMapper.paraTaskResponse(task);
	}
	
	public List<TaskResponseListUserDto> findByUserId(UUID id) {
		@SuppressWarnings("unused")
		UserEntity user= userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Usuario com id: "+id+" não encontrado"));
		
		List<TaskEntity> task= taskRepository.findByUser_id(id);
		
		return taskMapper.paraTaskResponseListUser(task);
	}
	
	public List<TaskResponseDto> findAll(){
		return taskMapper.paraListTaskResponse(taskRepository.findAll());
	}
	
	public TaskResponseDto update(UUID id , TaskRequestDto request) {
		TaskEntity task = taskRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Task com id: "+id+" não encontrado"));
		taskMapper.update(request, task);
		taskRepository.save(task);
		return taskMapper.paraTaskResponse(task);
	}
	
	public TaskResponseDto completeTask(UUID id) {
		TaskEntity task = taskRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Task com id: "+id+" não encontrado"));
		task.setCompleted(true);
		TaskEntity taskSalva=taskRepository.save(task);
		return taskMapper.paraTaskResponse(taskSalva);
	}
	
	public void deleteById(UUID id) {
		if(!taskRepository.existsById(id)) {
			throw new ResourceNotFoundException("Task com id: "+id+" não encontrado");
		}
		taskRepository.deleteById(id);
	}
	
}
