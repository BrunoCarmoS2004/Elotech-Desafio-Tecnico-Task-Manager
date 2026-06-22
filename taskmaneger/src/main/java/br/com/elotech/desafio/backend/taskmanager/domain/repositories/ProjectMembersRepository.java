package br.com.elotech.desafio.backend.taskmanager.domain.repositories;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.ProjectMembersGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.UserProjectStatus;
import br.com.elotech.desafio.backend.taskmanager.domain.models.ProjectMembers;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectMembersRepository extends JpaRepository<ProjectMembers, UUID> {
    <T> Optional<T> findById(UUID id, Class<T> type);
    <T> Optional<T> findByUserId(UUID memberId, Class<T> type);
    <T> Page<T> findBy(Pageable pageable, Class<T> type);

    Page<ProjectMembersGetDTO> findAllByProjectId(UUID projectId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE ProjectMembers pm SET pm.userProjectStatus = :userProjectStatus WHERE PM.id = :id")
    void changeUserProjectStatus(UserProjectStatus userProjectStatus, UUID id);
}
