package ru.practicum.ewm.main.user;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.main.user.dto.UserDTO;
import ru.practicum.ewm.main.user.dto.UserShortDTO;
import ru.practicum.ewm.main.user.dto.UserWithLikesDTO;

@UtilityClass
public class UserMapper {
    public User userDtoToUser(UserDTO dto) {
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public UserDTO userToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public UserShortDTO userToShortDTO(User user) {
        return UserShortDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public UserWithLikesDTO userToLikesDTO(User user, long likes, long dislikes) {
        return UserWithLikesDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .likes(likes)
                .dislikes(dislikes)
                .build();
    }
}
