package br.com.elotech.desafio.backend.taskmanager.domain.repositories;

import br.com.elotech.desafio.backend.taskmanager.domain.enums.EntityStatus;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.Role;
import br.com.elotech.desafio.backend.taskmanager.domain.models.User;
import br.com.elotech.desafio.backend.taskmanager.security.dtos.gets.UserLoginValidationGetDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    <T> Optional<T> findById(UUID id, Class<T> type);
    <T> Page<T> findBy(Pageable pageable, Class<T> type);

    Optional<UserLoginValidationGetDTO> findByEmail(String email);
    Boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.name = :name WHERE u.id = :id")
    void changeUserNameTo(String name, UUID id);
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.role = :role WHERE u.id = :id")
    void changeUserRoleTo(Role role, UUID id);
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.entityStatus = :entityStatus WHERE u.id = :id")
    void changeEntityStatusTo(EntityStatus entityStatus, UUID id);

    List<User> id(UUID id);

    Long countByIdIn(List<UUID> ids);
}
