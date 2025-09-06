package br.com.springEstudo.AutorTask.business.dto;

import java.util.UUID;

public record UserReferenceDto(UUID id,String nome, String email) {

}
