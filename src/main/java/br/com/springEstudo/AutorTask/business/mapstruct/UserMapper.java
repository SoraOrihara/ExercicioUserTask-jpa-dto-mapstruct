package br.com.springEstudo.AutorTask.business.mapstruct;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import br.com.springEstudo.AutorTask.business.dto.UserRequestDto;
import br.com.springEstudo.AutorTask.business.dto.UserResponseDto;
import br.com.springEstudo.AutorTask.infraestructure.entities.TaskEntity;
import br.com.springEstudo.AutorTask.infraestructure.entities.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mapping(target="tasks",ignore=true)
	UserEntity paraUserEntity(UserRequestDto request);
	
	@Mapping(source="tasks", target="tasksId")
	UserResponseDto paraUserResponseDto(UserEntity entity);
	
	 default Set<UUID> map(Set<TaskEntity> tasks) {
	        if (tasks == null) {
	            return null;
	        }
	        return tasks.stream()
	                .map(TaskEntity::getId)
	                .collect(Collectors.toSet());
	    }
	 
	List<UserResponseDto> paraListUserResponseDto(List<UserEntity> entity);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target="id",ignore=true)
	@Mapping(target="tasks",ignore=true)
	void updateUser(UserRequestDto request,@MappingTarget UserEntity entity);
}
