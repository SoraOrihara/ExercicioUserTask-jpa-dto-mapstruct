package br.com.springEstudo.AutorTask.infraestructure.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.springEstudo.AutorTask.business.dto.UserRequestDto;
import br.com.springEstudo.AutorTask.business.mapstruct.UserMapper;
import br.com.springEstudo.AutorTask.infraestructure.entities.UserEntity;
import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EntityManager entityManager;
	
	private UserMapper mapper = Mappers.getMapper(UserMapper.class);
	
	@Test
	@DisplayName("Should get User sucessfully from Database")
	void testFindByNomeCase1() {
		createUser(new UserRequestDto("Caio", "caio@gmail.com", "123"));
		Optional<UserEntity> foundedUser=userRepository.findByNome("Caio");
		assertThat(foundedUser.isPresent()).isTrue();
	}
	
	@Test
	@DisplayName("Should not getUserSucefully from database")
	void testFindByNomeCase2() {
		Optional<UserEntity> notFoundedUser=userRepository.findByNome("Jorge");
		assertThat(notFoundedUser.isEmpty()).isTrue();
	}
	
	private UserEntity createUser(UserRequestDto request){
		UserEntity user = mapper.paraUserEntity(request);
		entityManager.persist(user);
		return  user;
	}

}
