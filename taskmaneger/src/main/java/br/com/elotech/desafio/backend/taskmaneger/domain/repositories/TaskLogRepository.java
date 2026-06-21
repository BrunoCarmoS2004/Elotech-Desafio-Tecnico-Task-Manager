package br.com.elotech.desafio.backend.taskmaneger.domain.repositories;

import br.com.elotech.desafio.backend.taskmaneger.domain.models.TaskLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskLogRepository extends JpaRepository<TaskLog, UUID> {
}
