package br.com.springEstudo.AutorTask.business.dto;

import java.time.LocalDate;
import java.util.UUID;

public record TaskResponseDto(UUID id, String titulo, String descricao, Boolean completed, LocalDate dataConclusao, UUID userId) {

}
