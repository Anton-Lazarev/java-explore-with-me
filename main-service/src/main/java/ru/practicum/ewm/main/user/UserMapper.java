package ru.practicum.ewm.main.user;

import lombok.experimental.UtilityClass;

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
}
