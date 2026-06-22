package br.com.elotech.desafio.backend.taskmanager.mappers;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.UserGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.posts.UserPostDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserGetDTO userToUserGetDTO(User user);
    User userPostDTOToUser(UserPostDTO userPostDTO);
}
