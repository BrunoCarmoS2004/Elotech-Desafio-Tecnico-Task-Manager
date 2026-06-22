package br.com.elotech.desafio.backend.taskmanager.domain.Embedded;

import br.com.elotech.desafio.backend.taskmanager.domain.enums.EntityStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Embeddable
public class CommonData {
    @Enumerated(EnumType.STRING)
    @Column(name = "entity_status", nullable = false)
    private EntityStatus entityStatus = EntityStatus.ACTIVE;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
