package br.com.springEstudo.AutorTask.business.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public record TaskRequestDto(@NotBlank String titulo, @NotBlank String descricao, @NotBlank LocalDate dataConclusao) {

}
