package br.com.elotech.desafio.backend.taskmanager.mappers;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.ProjectGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.posts.ProjectPostDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.puts.ProjectPutDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.models.Project;
import br.com.elotech.desafio.backend.taskmanager.domain.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.UUID;

@Mapper(componentModel = "spring", uses =  {ProjectMembersMapper.class})
public interface ProjectMapper {
    ProjectGetDTO projectToProjectGetDTO(Project project);
    @Mapping(source = "creatorId", target = "creator")
    Project projectPostDTOToProject(ProjectPostDTO projectPostDTO);
    void projectPutDTOToProject(ProjectPutDTO projectPutDTO, @MappingTarget Project project);

    default User mapToUser(UUID creatorId) {
        User user = new User();
        user.setId(creatorId);
        return user;
    }
}
