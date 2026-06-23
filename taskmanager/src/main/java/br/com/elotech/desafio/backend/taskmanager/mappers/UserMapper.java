package br.com.elotech.desafio.backend.taskmanager.mappers;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.UserGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.posts.UserPostDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.models.User;
import br.com.elotech.desafio.backend.taskmanager.security.dtos.gets.UserWithTokenGetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mappings({
            @Mapping(source = "id", target = "user.id"),
            @Mapping(source = "name", target = "user.name"),
            @Mapping(source = "email", target = "user.email"),
            @Mapping(source = "role", target = "user.role"),
            @Mapping(source = "entityStatus", target = "user.entityStatus")
    })
    UserWithTokenGetDTO userToUserWithTokenGetDTO(User user);
    User userPostDTOToUser(UserPostDTO userPostDTO);
}
