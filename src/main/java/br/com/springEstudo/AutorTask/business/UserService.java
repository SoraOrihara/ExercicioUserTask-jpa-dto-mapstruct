package br.com.springEstudo.AutorTask.business;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.springEstudo.AutorTask.business.dto.UserRequestDto;
import br.com.springEstudo.AutorTask.business.dto.UserResponseDto;
import br.com.springEstudo.AutorTask.business.mapstruct.UserMapper;
import br.com.springEstudo.AutorTask.exceptions.ResourceNotFoundException;
import br.com.springEstudo.AutorTask.infraestructure.entities.UserEntity;
import br.com.springEstudo.AutorTask.infraestructure.repositories.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	public UserService(UserRepository userRepository,UserMapper userMapper) {
		this.userRepository=userRepository;
		this.userMapper=userMapper;
	}
	
	
	public List<UserResponseDto> findAll() {
		List<UserEntity> lista= userRepository.findAll();
		return userMapper.paraListUserResponseDto(lista);
	}
	
	public UserResponseDto findById(UUID id) {
		UserEntity user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Id "+id+" não encontrado"));
		return userMapper.paraUserResponseDto(user);
	}
	
	public void deleteById(UUID id) {
		userRepository.deleteById(id);
	}
	
	public UserResponseDto createUser(UserRequestDto request) {
		Optional<UserEntity> userExistente = userRepository.findByNome(request.nome());
		if(userExistente.isPresent()) {
			 new Exception("Username already exists: "+request.nome());
		}
		UserEntity user =userMapper.paraUserEntity(request);
		UserEntity savedUser = userRepository.save(user);
		return userMapper.paraUserResponseDto(savedUser);
	}
	
	public UserResponseDto updateUser(UUID id, UserRequestDto request) {
		UserEntity user = userRepository.getReferenceById(id);
		userMapper.updateUser(request, user);
		UserEntity userSalvo = userRepository.save(user);
		return userMapper.paraUserResponseDto(userSalvo);
	}
	
	
}
