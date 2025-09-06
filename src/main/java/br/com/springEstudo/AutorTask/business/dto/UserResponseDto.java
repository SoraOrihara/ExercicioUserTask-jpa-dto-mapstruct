package br.com.springEstudo.AutorTask.business.dto;

import java.util.Set;
import java.util.UUID;

public record UserResponseDto(UUID id, String nome,String email, Set<UUID> tasksId) {

}
