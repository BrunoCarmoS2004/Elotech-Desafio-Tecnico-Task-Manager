package br.com.elotech.desafio.backend.taskmanager.domain.repositories;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.TaskGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.TaskPriority;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.TaskStatus;
import br.com.elotech.desafio.backend.taskmanager.domain.models.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task> {

    int countByResponsibleIdAndStatus(UUID responsibleId, TaskStatus taskStatus);

    @Query("SELECT new br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.TaskGetDTO(" +
            "t.id, " +
            "t.title, " +
            "t.description, " +
            "t.status, " +
            "t.priority, " +
            "t.deadline, " +
            "t.responsible.id, " +
            "t.project.id, " +
            "t.entityStatus, " +
            "t.commonData.createdAt," +
            "t.commonData.updatedAt) FROM Task t WHERE t.id = :id")
    Optional<TaskGetDTO> findTaskById(UUID id);

    @Query("SELECT new br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.TaskGetDTO(" +
            "t.id, " +
            "t.title, " +
            "t.description, " +
            "t.status, " +
            "t.priority, " +
            "t.deadline, " +
            "t.responsible.id, " +
            "t.project.id, " +
            "t.entityStatus, " +
            "t.commonData.createdAt," +
            "t.commonData.updatedAt) FROM Task t")
    Page<TaskGetDTO> findAllTasks(Pageable pageable);

    @Query("SELECT t.status, COUNT(t) FROM Task t WHERE t.project.id = :projectId AND t.entityStatus = 'ACTIVE' GROUP BY t.status")
    List<Object[]> countTasksByStatusGrouped(UUID projectId);

    @Query("SELECT t.priority, COUNT(t) FROM Task t WHERE t.project.id = :projectId AND t.entityStatus = 'ACTIVE' GROUP BY t.priority")
    List<Object[]> countTasksByPriorityGrouped(UUID projectId);

    @Transactional
    @Modifying
    @Query("UPDATE Task t SET t.status = :taskStatus WHERE t.id = :id")
    void changeTaskStatusTo(TaskStatus taskStatus, UUID id);

    @Transactional
    @Modifying
    @Query("UPDATE Task t SET t.priority = :taskPriority WHERE t.id = :id")
    void changeTaskPriorityTo(TaskPriority taskPriority, UUID id);

    @Transactional
    @Modifying
    @Query("UPDATE Task t SET t.responsible.id = :responsibleId WHERE t.id = :id")
    void changeResponsibleTo(UUID responsibleId, UUID id);
}
