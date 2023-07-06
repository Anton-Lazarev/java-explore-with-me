package ru.practicum.ewm.main.user.service;

import ru.practicum.ewm.main.user.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getPageOfUsers(List<Long> ids, int from, int size);

    UserDTO addUser(UserDTO dto);

    void deleteUserByID(long id);
}
