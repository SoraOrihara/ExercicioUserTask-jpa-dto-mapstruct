package br.com.springEstudo.AutorTask.business.mapstruct;

import java.util.List;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.springEstudo.AutorTask.business.dto.TaskRequestDto;
import br.com.springEstudo.AutorTask.business.dto.TaskResponseDto;
import br.com.springEstudo.AutorTask.business.dto.TaskResponseListUserDto;

import br.com.springEstudo.AutorTask.infraestructure.entities.TaskEntity;


@Mapper(componentModel = "spring")
public interface TaskMapper {
	
	@Mapping(target="user",ignore=true)
	@Mapping(target="dataCriacao",ignore=true)
	@Mapping(target="completed",ignore=true)
	TaskEntity paraTaskEntity(TaskRequestDto request);
	
	@Mapping(source="user.id",target="userId")
	TaskResponseDto paraTaskResponse(TaskEntity entity);
	
	@Mapping(source="user",target="user")
	TaskResponseListUserDto paraTaskResponseListUser(TaskEntity entity);
	
	
	List<TaskResponseDto> paraListTaskResponse(List<TaskEntity> entity);
}
