package br.com.elotech.desafio.backend.taskmanager.domain.models;

import br.com.elotech.desafio.backend.taskmanager.domain.enums.UserProjectStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "project_members")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_project_status")
    private UserProjectStatus userProjectStatus = UserProjectStatus.ACTIVE;

    public ProjectMembers(Project project, User user) {
        this.project = project;
        this.user = user;
    }
}
