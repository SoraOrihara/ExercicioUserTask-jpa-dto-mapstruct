package br.com.springEstudo.AutorTask.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.springEstudo.AutorTask.business.dto.UserRequestDto;
import br.com.springEstudo.AutorTask.business.dto.UserResponseDto;
import br.com.springEstudo.AutorTask.business.mapstruct.UserMapper;
import br.com.springEstudo.AutorTask.exceptions.ResourceNotFoundException;
import br.com.springEstudo.AutorTask.infraestructure.entities.UserEntity;
import br.com.springEstudo.AutorTask.infraestructure.repositories.UserRepository;

class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private UserMapper userMapper;

	@Autowired
	@InjectMocks
	private UserService userService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindAll() {
		// Arrange (Preparar o cenário)
		UserEntity user1 = new UserEntity("Maria", "maria@email.com", "1234");
		UserEntity user2 = new UserEntity("João", "joao@email.com", "1234");
		List<UserEntity> userEntities = Arrays.asList(user1, user2);

		UserResponseDto dto1 = userMapper.paraUserResponseDto(user1);
		UserResponseDto dto2 = userMapper.paraUserResponseDto(user2);
		List<UserResponseDto> expectedDtos = Arrays.asList(dto1, dto2);

		when(userRepository.findAll()).thenReturn(userEntities);
		when(userMapper.paraListUserResponseDto(userEntities)).thenReturn(expectedDtos);

		// Act (Executar a ação)
		List<UserResponseDto> result = userService.findAll();

		// Assert (Verificar o resultado)
		assertNotNull(result);
		assertEquals(expectedDtos.size(), result.size());
		verify(userRepository, times(1)).findAll();
		verify(userMapper, times(1)).paraListUserResponseDto(userEntities);
	}

	@Test
	@DisplayName("Should deleteById a user")
	void testDeleteByIdCase1() {
		// Preparar
		UUID id = UUID.randomUUID();
		when(userRepository.existsById(id)).thenReturn(true);

		// Ação
		userService.deleteById(id);

		// Verificação
		verify(userRepository, times(1)).existsById(id);
		verify(userRepository, times(1)).deleteById(id);
	}

	@Test
	@DisplayName("Should throw a ResourceNotFoundException when Id not found")
	void testDeleteByIdCase2() {
		// Preparar
		UUID id = UUID.randomUUID();
		when(userRepository.existsById(id)).thenReturn(false);

		// Ação
		assertThrows(ResourceNotFoundException.class, () -> userService.deleteById(id));

		// Verificação
		verify(userRepository, times(1)).existsById(id);

		verify(userRepository, never()).deleteById(id);
	}

	@Test
	@DisplayName("Should return a userResponseDto")
	void testCreateUser() {
		//Preparação
		UserRequestDto request = new UserRequestDto("Caio", "caio@gmail", "123");
		UserEntity entity = new UserEntity("Caio","caio@gmail","123");
		UserEntity savedEntity = new UserEntity(UUID.randomUUID(),"Caio","caio@gmail","123");
		UserResponseDto response = new UserResponseDto(savedEntity.getId(),"Caio","caio@gmail",new HashSet<>());
		
		when(userRepository.findByNome(request.nome())).thenReturn(Optional.empty());
		when(userMapper.paraUserEntity(request)).thenReturn(entity);
		when(userRepository.save(entity)).thenReturn(savedEntity);
		when(userMapper.paraUserResponseDto(savedEntity)).thenReturn(response);
		//Ação
		userService.createUser(request);
		
		//Verificar
		verify(userRepository,times(1)).findByNome(request.nome());
		verify(userMapper, times(1)).paraUserEntity(request);
		verify(userRepository,times(1)).save(entity);
		verify(userMapper,times(1)).paraUserResponseDto(savedEntity);
		
	}
	@Test
	@DisplayName("Should return a Exception")
	void testCreateUserCase2() {
		//Preparação
		UserRequestDto request = new UserRequestDto("Caio", "caio@gmail", "123");
		UserEntity entity = new UserEntity("Caio","caio@gmail","123");
		
		when(userRepository.findByNome(request.nome())).thenReturn(Optional.of(entity));
		
		//Ação
		assertThrows(IllegalArgumentException.class,()->userService.createUser(request));
		
		//Verificar
		verify(userRepository,times(1)).findByNome(request.nome());
		
	}

	@Test
	@DisplayName("Should return a userResponseDto")
	void testUpdateUser() {
		UUID id=UUID.randomUUID();
		// Crie o DTO de requisição com os dados de atualização
		UserRequestDto request = new UserRequestDto("nomeNovo", "emailNovo", null);
		// Simule a entidade de usuário existente no banco de dados
		UserEntity usuarioExistente = new UserEntity(id,"nomeAntigo","emailAntigo","senha");
		// Simule a entidade que seria retornada após a atualização
		UserEntity usuarioNovo = new UserEntity(id,request.nome(),request.email(),usuarioExistente.getSenha());
		//Dto resposta esperado
		UserResponseDto response = new UserResponseDto(usuarioNovo.getId(),usuarioNovo.getNome(),usuarioNovo.getEmail(), new HashSet<>());
		
		// Configure o mock do repositório
	    // Quando findById é chamado, ele retorna um Optional com o usuário existente
		when(userRepository.existsById(id)).thenReturn(true);
	    when(userRepository.getReferenceById(id)).thenReturn(usuarioExistente);
	    when(userRepository.save(usuarioExistente)).thenReturn(usuarioNovo);
	    when(userMapper.paraUserResponseDto(usuarioNovo)).thenReturn(response);
	    
	   
	    // A simulação do userMapper.updateUser é feita implicitamente, pois ele é void.

	    // 2. Ação (Act)
	    UserResponseDto result = userService.updateUser(id, request);

	    // 3. Verificação (Assert)
	    assertNotNull(result);
	    assertEquals(response.nome(), result.nome());
	    assertEquals(response.id(), result.id());

	    // Verifique se os métodos foram chamados
	    verify(userRepository, times(1)).getReferenceById(id);
	    verify(userMapper, times(1)).updateUser(request, usuarioExistente);
	    verify(userRepository, times(1)).save(usuarioExistente);
	    verify(userMapper, times(1)).paraUserResponseDto(usuarioNovo);
		
		
	}

}
