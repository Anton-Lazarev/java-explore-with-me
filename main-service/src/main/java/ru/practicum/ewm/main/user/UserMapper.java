package ru.practicum.ewm.main.user;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.main.user.dto.UserDTO;
import ru.practicum.ewm.main.user.dto.UserShortDTO;

@UtilityClass
public class UserMapper {
    public User UserDtoToUser(UserDTO dto) {
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
}
