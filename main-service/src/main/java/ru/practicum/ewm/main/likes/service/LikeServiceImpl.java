package ru.practicum.ewm.main.likes.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.event.Event;
import ru.practicum.ewm.main.event.EventRepository;
import ru.practicum.ewm.main.exceptions.EventNotFoundException;
import ru.practicum.ewm.main.exceptions.IncorrectUserForLikeException;
import ru.practicum.ewm.main.exceptions.LikeAlreadyExistException;
import ru.practicum.ewm.main.exceptions.UserNotFoundException;
import ru.practicum.ewm.main.likes.Like;
import ru.practicum.ewm.main.likes.LikeDTO;
import ru.practicum.ewm.main.likes.LikeMapper;
import ru.practicum.ewm.main.likes.LikeRepository;
import ru.practicum.ewm.main.requests.EventRequest;
import ru.practicum.ewm.main.requests.EventRequestRepository;
import ru.practicum.ewm.main.requests.dto.EventRequestStatus;
import ru.practicum.ewm.main.user.User;
import ru.practicum.ewm.main.user.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class LikeServiceImpl implements LikeService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventRequestRepository requestRepository;
    private final LikeRepository likeRepository;

    @Override
    @Transactional
    public LikeDTO addLike(long userID, long eventID, boolean isLike) {
        Optional<User> user = userRepository.findById(userID);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with ID " + userID + " not presented");
        }
        Optional<Event> event = eventRepository.findById(eventID);
        if (event.isEmpty()) {
            throw new EventNotFoundException("Event with ID " + eventID + " not presented");
        }
        if (event.get().getInitiator().getId() == user.get().getId()) {
            throw new IncorrectUserForLikeException("Initiator can't like own events");
        }
        Optional<EventRequest> request = requestRepository.findByRequesterIdAndEventIdAndStatus(userID, eventID, EventRequestStatus.CONFIRMED);
        if (request.isEmpty()) {
            throw new IncorrectUserForLikeException("User with ID " + userID + " not participated in event with ID " + eventID);
        }
        Optional<Like> like = likeRepository.findByUserIdAndEventId(userID, eventID);
        if (like.isPresent()) {
            throw new LikeAlreadyExistException("User with ID " + userID + " already like event with ID " + eventID);
        }
        Like newLike = likeRepository.save(Like.builder()
                .user(user.get())
                .event(event.get())
                .isLike(isLike)
                .created(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build());
        log.info("Like added from user with ID {} to event with ID {}", userID, eventID);
        return LikeMapper.likeToLikeDTO(newLike);
    }

    @Override
    @Transactional
    public void deleteLike(long userID, long eventID) {
        if (!userRepository.existsById(userID)) {
            throw new UserNotFoundException("User with ID " + userID + " not presented");
        }
        if (!eventRepository.existsById(eventID)) {
            throw new EventNotFoundException("Event with ID " + eventID + " not presented");
        }
        Optional<Like> like = likeRepository.findByUserIdAndEventId(userID, eventID);
        like.ifPresent(value -> likeRepository.deleteById(value.getId()));
        log.info("Deleting like from user with ID {} to event with ID {}", userID, eventID);
    }
}
