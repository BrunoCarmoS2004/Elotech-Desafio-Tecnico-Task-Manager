package br.com.elotech.desafio.backend.taskmanager.domain.repositories;

import br.com.elotech.desafio.backend.taskmanager.domain.models.TaskLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskLogRepository extends JpaRepository<TaskLog, UUID> {
}
