package ru.practicum.ewm.main.user.service;

import ru.practicum.ewm.main.user.dto.UserDTO;
import ru.practicum.ewm.main.user.dto.UserWithLikesDTO;

import java.util.List;

public interface UserService {
    List<UserWithLikesDTO> getPageOfUsers(List<Long> ids, int from, int size);

    UserDTO addUser(UserDTO dto);

    void deleteUserByID(long id);

    UserWithLikesDTO getUserByID(long userID);
}
