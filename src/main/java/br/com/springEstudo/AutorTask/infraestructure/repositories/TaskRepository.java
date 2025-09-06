package br.com.springEstudo.AutorTask.infraestructure.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.springEstudo.AutorTask.infraestructure.entities.TaskEntity;



@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {

	List<TaskEntity> findByUser_id(UUID userId);
}
