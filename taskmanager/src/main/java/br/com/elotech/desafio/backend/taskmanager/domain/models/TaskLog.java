package br.com.elotech.desafio.backend.taskmanager.domain.models;

import br.com.elotech.desafio.backend.taskmanager.domain.Embedded.CommonData;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.EntityStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tasks_logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank
    @Column(name = "altered_field", nullable = false, length = 50)
    private String alteredField;

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    @NotNull
    @Column(name = "altered_date", nullable = false)
    private LocalDateTime alteredDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_status", nullable = false)
    private EntityStatus entityStatus = EntityStatus.ACTIVE;

    @Embedded
    private CommonData commonData = new CommonData();

    public TaskLog(Task task, User user, String alteredField, String oldValue, String newValue) {
        this.task = task;
        this.user = user;
        this.alteredField = alteredField;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.alteredDate = LocalDateTime.now();
    }
}
