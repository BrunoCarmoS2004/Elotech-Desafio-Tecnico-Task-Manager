package br.com.elotech.desafio.backend.taskmanager.services;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.TaskLogGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.models.TaskLog;
import br.com.elotech.desafio.backend.taskmanager.domain.repositories.TaskLogRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TaskLogService {
    private final TaskLogRepository taskLogRepository;

    public TaskLogService(TaskLogRepository taskLogRepository) {
        this.taskLogRepository = taskLogRepository;
    }

    public void saveTaskLog(TaskLog taskLog) {
        taskLogRepository.save(taskLog);
    }

    public PagedModel<TaskLogGetDTO> getAllTaskLogsByTaskId(UUID taskId, Pageable pageable) {
        return new PagedModel<>(taskLogRepository.findAllTaskLogsByTaskId(taskId, pageable));
    }
}
