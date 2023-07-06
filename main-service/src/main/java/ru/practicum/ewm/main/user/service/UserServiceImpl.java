package ru.practicum.ewm.main.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.Paginator;
import ru.practicum.ewm.main.exceptions.UserNotFoundException;
import ru.practicum.ewm.main.user.User;
import ru.practicum.ewm.main.user.UserMapper;
import ru.practicum.ewm.main.user.UserRepository;
import ru.practicum.ewm.main.user.dto.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    @Transactional
    public UserDTO addUser(UserDTO dto) {
        User newUser = repository.save(UserMapper.UserDtoToUser(dto));
        log.info("Created user with ID: {}, and email: {}", newUser.getId(), newUser.getEmail());
        return UserMapper.userToUserDTO(newUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getPageOfUsers(List<Long> ids, int from, int size) {
        List<UserDTO> dtos;
        Pageable pageable = new Paginator(from, size);
        if (ids == null || ids.isEmpty()) {
            dtos = repository.findAll(pageable)
                    .stream().map(UserMapper::userToUserDTO).collect(Collectors.toList());
        } else {
            dtos = repository.finAllByIDs(ids, pageable)
                    .stream().map(UserMapper::userToUserDTO).collect(Collectors.toList());
        }
        log.info("Got list of users with size {}", dtos.size());
        return dtos;
    }

    @Override
    @Transactional
    public void deleteUserByID(long id) {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException("User with ID " + id + " not presented");
        }
        log.info("User with ID {} deleted", id);
        repository.deleteById(id);
    }
}
