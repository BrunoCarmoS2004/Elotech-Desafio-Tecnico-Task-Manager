package br.com.elotech.desafio.backend.taskmanager.mappers;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.ProjectMembersGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.models.ProjectMembers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ProjectMembersMapper {
    @Mappings({
            @Mapping(source = "project.id", target = "projectId"),
            @Mapping(source = "user.id", target = "userId")
    })
    ProjectMembersGetDTO projectMembersToProjectMembersGetDTO(ProjectMembers projectMembers);
}
