package br.com.elotech.desafio.backend.taskmanager.domain.models;

import br.com.elotech.desafio.backend.taskmanager.domain.Embedded.CommonData;
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

    @Embedded
    private CommonData commonData = new CommonData();

}
