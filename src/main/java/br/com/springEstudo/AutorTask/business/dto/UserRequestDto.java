package br.com.springEstudo.AutorTask.business.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRequestDto(@NotBlank String nome, @NotBlank String email, @NotBlank String senha) {

}
