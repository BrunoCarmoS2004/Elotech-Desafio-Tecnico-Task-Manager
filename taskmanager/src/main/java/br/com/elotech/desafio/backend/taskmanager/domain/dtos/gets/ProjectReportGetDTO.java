package br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets;

import br.com.elotech.desafio.backend.taskmanager.domain.enums.TaskPriority;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.TaskStatus;

import java.util.Map;

public record ProjectReportGetDTO(
      Map<TaskStatus, Long> byStatus,
      Map<TaskPriority, Long> byPriority
) {
}
