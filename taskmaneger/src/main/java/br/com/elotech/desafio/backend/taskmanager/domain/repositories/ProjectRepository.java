package br.com.elotech.desafio.backend.taskmanager.domain.repositories;

import br.com.elotech.desafio.backend.taskmanager.domain.models.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    <T> Optional<T> findById(UUID id, Class<T> type);

    @EntityGraph(attributePaths = {"members", "creator"})
    @Query("SELECT p FROM Project p")
    Page<Project> findAllProjects(Pageable pageable);

    @EntityGraph(attributePaths = {"creator"})
    Page<Project> findByCreatorId(UUID creatorId, Pageable pageable);

    @EntityGraph(attributePaths = {"members", "creator"})
    Page<Project> findDistinctByMembersUserId(UUID userId, Pageable pageable);

    boolean existsByName(String name);
}
