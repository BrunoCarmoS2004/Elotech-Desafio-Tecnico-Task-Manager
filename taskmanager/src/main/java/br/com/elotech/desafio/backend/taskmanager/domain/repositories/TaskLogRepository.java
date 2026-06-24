package br.com.elotech.desafio.backend.taskmanager.domain.repositories;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.TaskGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.TaskLogGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.models.TaskLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskLogRepository extends JpaRepository<TaskLog, UUID> {
    Optional<TaskLogGetDTO> findAllByTaskId(UUID taskId);

    @Query("SELECT new br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.TaskLogGetDTO(" +
            "tl.id, " +
            "tl.task.id, " +
            "tl.user.id, " +
            "tl.alteredField, " +
            "tl.oldValue, " +
            "tl.newValue, " +
            "tl.alteredDate) FROM TaskLog tl WHERE tl.task.id = :id")
    Page<TaskLogGetDTO> findAllTaskLogsByTaskId(UUID id, Pageable pageable);
}
