package br.com.springEstudo.AutorTask.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.springEstudo.AutorTask.business.TaskService;
import br.com.springEstudo.AutorTask.business.dto.TaskRequestDto;
import br.com.springEstudo.AutorTask.business.dto.TaskResponseDto;
import br.com.springEstudo.AutorTask.business.dto.TaskResponseListUserDto;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

	private final TaskService taskService;
	
	public TaskController(TaskService taskService) {
		this.taskService=taskService;
	}
	
	/**
     * POST /api/tasks/{userId}: Cria uma nova tarefa para um usuário.
     */
    @PostMapping("/{userId}")
    public ResponseEntity<TaskResponseDto> createTask(@PathVariable UUID userId, @RequestBody TaskRequestDto request) {
        TaskResponseDto createdTask = taskService.createTask(userId, request);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTask.id())
                .toUri();
                
        return ResponseEntity.created(location).body(createdTask);
    }
    
    /**
     * GET /api/tasks: Retorna todas as tarefas (opcional, para testes ou admin).
     */
    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        List<TaskResponseDto> tasks = taskService.findAll();
        return ResponseEntity.ok(tasks);
    }

    /**
     * GET /api/tasks/{taskId}: Retorna uma tarefa por ID.
     */
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable UUID taskId) {
        TaskResponseDto task = taskService.findTaskById(taskId);
        return ResponseEntity.ok(task);
    }

    /**
     * GET /api/tasks/by-user/{userId}: Retorna todas as tarefas de um usuário.
     */
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<TaskResponseListUserDto>> getTasksByUserId(@PathVariable UUID userId) {
        List<TaskResponseListUserDto> tasks = taskService.findByUserId(userId);
        return ResponseEntity.ok(tasks);
    }

    /**
     * PUT /api/tasks/{taskId}: Atualiza uma tarefa por completo.
     */
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable UUID taskId, @RequestBody TaskRequestDto request) {
        TaskResponseDto updatedTask = taskService.update(taskId, request);
        return ResponseEntity.ok(updatedTask);
    }

    /**
     * PATCH /api/tasks/{taskId}/complete: Marca uma tarefa como concluída.
     */
    @PatchMapping("/{taskId}/complete")
    public ResponseEntity<TaskResponseDto> completeTask(@PathVariable UUID taskId) {
        TaskResponseDto completedTask = taskService.completeTask(taskId);
        return ResponseEntity.ok(completedTask);
    }

    /**
     * DELETE /api/tasks/{taskId}: Deleta uma tarefa.
     */
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID taskId) {
        taskService.deleteById(taskId);
        return ResponseEntity.noContent().build();
    }
	
	
}
