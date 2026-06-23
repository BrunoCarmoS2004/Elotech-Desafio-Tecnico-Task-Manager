package br.com.elotech.desafio.backend.taskmanager.mappers;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.TaskGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.posts.TaskPostDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.puts.TaskPutDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.models.Project;
import br.com.elotech.desafio.backend.taskmanager.domain.models.Task;
import br.com.elotech.desafio.backend.taskmanager.domain.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mappings({
            @Mapping(source = "project.id", target = "projectId"),
            @Mapping(source = "responsible.id", target = "responsibleId"),
            @Mapping(source = "commonData.createdAt", target = "createdAt"),
            @Mapping(source = "commonData.updatedAt", target = "updatedAt")
    })
    TaskGetDTO taskToTaskGetDTO(Task task);

    @Mappings({
            @Mapping(source = "responsibleId", target = "responsible"),
            @Mapping(source = "projectId", target = "project")
    })
    Task taskPostDTOToTask(TaskPostDTO taskPostDTO);

    void taskPutDTOToTask(TaskPutDTO taskPutDTO, @MappingTarget Task task);

    default Project mapToProject(UUID projectId) {
        Project project = new Project();
        project.setId(projectId);
        return project;
    }

    default User mapToUser(UUID responsibleId) {
        if (responsibleId == null){
            return null;
        }
        User user = new User();
        user.setId(responsibleId);
        return user;
    }
}
