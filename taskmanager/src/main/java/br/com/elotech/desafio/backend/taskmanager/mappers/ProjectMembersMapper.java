package br.com.elotech.desafio.backend.taskmanager.mappers;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.ProjectMembersGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.models.ProjectMembers;
import br.com.elotech.desafio.backend.taskmanager.domain.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ProjectMembersMapper {
    @Mappings({
            @Mapping(source = "project.id", target = "projectId"),
            @Mapping(source = "user.id", target = "userId")
    })
    ProjectMembersGetDTO projectMembersToProjectMembersGetDTO(ProjectMembers projectMembers);

    default ProjectMembers mapToProjectMembers(UUID userId) {
        User user = new User();
        user.setId(userId);
        ProjectMembers projectMember = new ProjectMembers();
        projectMember.setUser(user);
        return projectMember;
    }
}
