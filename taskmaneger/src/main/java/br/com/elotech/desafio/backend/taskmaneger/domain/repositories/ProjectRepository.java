package br.com.elotech.desafio.backend.taskmaneger.domain.repositories;

import br.com.elotech.desafio.backend.taskmaneger.domain.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
}
