package br.com.elotech.desafio.backend.taskmaneger.mappers;

import br.com.elotech.desafio.backend.taskmaneger.domain.dtos.gets.UserGetDTO;
import br.com.elotech.desafio.backend.taskmaneger.domain.dtos.posts.UserPostDTO;
import br.com.elotech.desafio.backend.taskmaneger.domain.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserGetDTO userToUserGetDTO(User user);
    User userPostDTOToUser(UserPostDTO userPostDTO);
}
