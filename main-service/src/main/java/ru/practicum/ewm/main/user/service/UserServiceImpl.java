package ru.practicum.ewm.main.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.Paginator;
import ru.practicum.ewm.main.exceptions.UserNotFoundException;
import ru.practicum.ewm.main.likes.LikeRepository;
import ru.practicum.ewm.main.user.User;
import ru.practicum.ewm.main.user.UserMapper;
import ru.practicum.ewm.main.user.UserRepository;
import ru.practicum.ewm.main.user.dto.UserDTO;
import ru.practicum.ewm.main.user.dto.UserWithLikesDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    @Override
    @Transactional
    public UserDTO addUser(UserDTO dto) {
        User newUser = userRepository.save(UserMapper.userDtoToUser(dto));
        log.info("Created user with ID: {}, and email: {}", newUser.getId(), newUser.getEmail());
        return UserMapper.userToUserDTO(newUser);
    }

    @Override
    public List<UserWithLikesDTO> getPageOfUsers(List<Long> ids, int from, int size) {
        List<UserWithLikesDTO> dtos;
        Pageable pageable = new Paginator(from, size);
        if (ids == null || ids.isEmpty()) {
            dtos = collectUsersWithLikes(userRepository.findAll(pageable).toList());
        } else {
            dtos = collectUsersWithLikes(userRepository.finAllByIDs(ids, pageable));
        }
        log.info("Got list of users with size {}", dtos.size());
        return dtos;
    }

    @Override
    public UserWithLikesDTO getUserByID(long userID) {
        Optional<User> user = userRepository.findById(userID);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with ID " + userID + " not presented");
        }
        log.info("Got single user with ID: {} and name: {}", user.get().getId(), user.get().getName());
        long likes = likeRepository.countLikesByUserIdAndStatus(userID, true);
        long dislikes = likeRepository.countLikesByUserIdAndStatus(userID, false);
        return UserMapper.userToLikesDTO(user.get(), likes, dislikes);
    }

    @Override
    @Transactional
    public void deleteUserByID(long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with ID " + id + " not presented");
        }
        log.info("User with ID {} deleted", id);
        userRepository.deleteById(id);
    }

    private List<UserWithLikesDTO> collectUsersWithLikes(List<User> users) {
        List<UserWithLikesDTO> dtos = new ArrayList<>();
        for (User user : users) {
            long likes = likeRepository.countLikesByUserIdAndStatus(user.getId(), true);
            long dislikes = likeRepository.countLikesByUserIdAndStatus(user.getId(), false);
            dtos.add(UserMapper.userToLikesDTO(user, likes, dislikes));
        }
        return dtos;
    }
}
