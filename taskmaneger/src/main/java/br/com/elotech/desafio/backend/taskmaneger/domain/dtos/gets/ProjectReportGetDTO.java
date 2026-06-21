package br.com.elotech.desafio.backend.taskmaneger.domain.dtos.gets;

import br.com.elotech.desafio.backend.taskmaneger.domain.enums.TaskPriority;
import br.com.elotech.desafio.backend.taskmaneger.domain.enums.TaskStatus;

import java.util.Map;

public record ProjectReportGetDTO(
      Map<TaskStatus, Long> byStatus,
      Map<TaskPriority, Long> byPriority
) {
}
