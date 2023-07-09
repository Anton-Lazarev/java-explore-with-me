package ru.practicum.ewm.main.requests.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.event.Event;
import ru.practicum.ewm.main.event.EventRepository;
import ru.practicum.ewm.main.event.dto.EventStatus;
import ru.practicum.ewm.main.exceptions.ConflictStatusException;
import ru.practicum.ewm.main.exceptions.EventNotFoundException;
import ru.practicum.ewm.main.exceptions.EventRequestNotFoundException;
import ru.practicum.ewm.main.exceptions.IncorrectOwnerOfEventException;
import ru.practicum.ewm.main.exceptions.IncorrectRequesterException;
import ru.practicum.ewm.main.exceptions.MemberLimitException;
import ru.practicum.ewm.main.exceptions.RequestAlreadyExistException;
import ru.practicum.ewm.main.exceptions.UserNotFoundException;
import ru.practicum.ewm.main.requests.EventRequest;
import ru.practicum.ewm.main.requests.EventRequestMapper;
import ru.practicum.ewm.main.requests.EventRequestRepository;
import ru.practicum.ewm.main.requests.dto.ChangeStatusRequestsDTO;
import ru.practicum.ewm.main.requests.dto.EventRequestDTO;
import ru.practicum.ewm.main.requests.dto.EventRequestStatus;
import ru.practicum.ewm.main.requests.dto.OutcomeGroupedRequestsDTO;
import ru.practicum.ewm.main.user.User;
import ru.practicum.ewm.main.user.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class EventRequestServiceImpl implements EventRequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventRequestRepository requestRepository;

    @Override
    public EventRequestDTO addEventRequest(long userID, long eventID) {
        Optional<User> user = userRepository.findById(userID);
        Optional<Event> event = eventRepository.findById(eventID);
        checkExistenceUserAndEvent(userID, eventID, user, event);
        if (!event.get().getState().equals(EventStatus.PUBLISHED)) {
            throw new ConflictStatusException("Can't add request to unpublished event");
        }
        if (event.get().getInitiator().getId() == user.get().getId()) {
            throw new ConflictStatusException("Initiator of event can't add request to it");
        }
        if (requestRepository.findByRequesterIdAndEventId(userID, eventID).isPresent()) {
            throw new RequestAlreadyExistException("Request already exist with same params");
        }
        long currentRequests = requestRepository.countAllByEventIdAndStatus(eventID, EventRequestStatus.CONFIRMED);
        if (event.get().getMembersLimit() > 0 && event.get().getMembersLimit() <= currentRequests) {
            throw new MemberLimitException("Can't add request because of event members limit was full");
        }

        EventRequestStatus status;
        if (!event.get().isRequestModeration() || event.get().getMembersLimit() == 0) {
            status = EventRequestStatus.CONFIRMED;
        } else {
            status = EventRequestStatus.PENDING;
        }
        EventRequest newRequest = requestRepository.save(EventRequest.builder()
                .event(event.get())
                .requester(user.get())
                .status(status)
                .created(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build());
        log.info("Save new request with ID {} for event with ID {} from user with ID {}", newRequest.getId(), eventID, userID);
        return EventRequestMapper.requestToEventRequestDTO(newRequest);
    }

    @Override
    public EventRequestDTO cancelRequestByRequester(long userID, long reqID) {
        Optional<User> user = userRepository.findById(userID);
        Optional<EventRequest> request = requestRepository.findById(reqID);
        if (request.isEmpty()) {
            throw new EventRequestNotFoundException("Request with ID " + reqID + " not present");
        }
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with ID " + userID + " not presented");
        }
        if (request.get().getRequester().getId() != user.get().getId()) {
            throw new IncorrectRequesterException("User with ID " + userID + " not requester in request with ID " + reqID);
        }
        request.get().setStatus(EventRequestStatus.CANCELED);
        requestRepository.save(request.get());
        log.info("User with ID {} was cancel his request for event with ID {}", userID, reqID);
        return EventRequestMapper.requestToEventRequestDTO(request.get());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventRequestDTO> getAllRequestsOfRequester(long userID) {
        if (!userRepository.existsById(userID)) {
            throw new UserNotFoundException("User with ID " + userID + " not presented");
        }
        List<EventRequestDTO> dtos = requestRepository.findAllByRequesterId(userID).stream()
                .map(EventRequestMapper::requestToEventRequestDTO).collect(Collectors.toList());
        log.info("Getting list requests of requester with ID {} with size {}", userID, dtos.size());
        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventRequestDTO> getAllRequestsToEventByEventInitiator(long userID, long eventID) {
        Optional<User> user = userRepository.findById(userID);
        Optional<Event> event = eventRepository.findById(eventID);
        checkExistenceAndInitiator(userID, eventID, user, event);

        List<EventRequestDTO> dtos = requestRepository.findAllByEventId(eventID).stream()
                .map(EventRequestMapper::requestToEventRequestDTO).collect(Collectors.toList());
        log.info("Getting list of requests for initiator with ID {} and event with ID {} and size {}", userID, eventID, dtos.size());
        return dtos;
    }

    @Override
    public OutcomeGroupedRequestsDTO changeStatusOfRequestsByEventInitiator(long userID, long eventID, ChangeStatusRequestsDTO dto) {
        Optional<User> user = userRepository.findById(userID);
        Optional<Event> event = eventRepository.findById(eventID);
        checkExistenceAndInitiator(userID, eventID, user, event);

        List<EventRequestDTO> confirmedRequests = new ArrayList<>();
        List<EventRequestDTO> rejectedRequests = new ArrayList<>();
        long currentApprovedRequests = requestRepository.countAllByEventIdAndStatus(eventID, EventRequestStatus.CONFIRMED);
        if (currentApprovedRequests == event.get().getMembersLimit()) {
            throw new MemberLimitException("Limit to event with ID " + eventID + " already full");
        }

        List<EventRequest> requests = requestRepository.findAllByEventIdAndIdIn(eventID, dto.getRequestIds());
        if (dto.getStatus().equals(EventRequestStatus.CONFIRMED)) {
            for (EventRequest currRequest : requests) {
                if (currRequest.getStatus() != EventRequestStatus.PENDING) {
                    throw new ConflictStatusException("For changing status it should be PENDING");
                }
                if (currentApprovedRequests < event.get().getMembersLimit()) {
                    currRequest.setStatus(EventRequestStatus.CONFIRMED);
                    currentApprovedRequests++;
                    confirmedRequests.add(EventRequestMapper.requestToEventRequestDTO(currRequest));
                } else {
                    currRequest.setStatus(EventRequestStatus.REJECTED);
                    rejectedRequests.add(EventRequestMapper.requestToEventRequestDTO(currRequest));
                }
            }
        } else {
            for (EventRequest currRequest : requests) {
                if (currRequest.getStatus() != EventRequestStatus.PENDING) {
                    throw new ConflictStatusException("For changing status it should be PENDING");
                }
                currRequest.setStatus(EventRequestStatus.REJECTED);
                rejectedRequests.add(EventRequestMapper.requestToEventRequestDTO(currRequest));
            }
        }
        requestRepository.saveAll(requests);
        log.info("Confirm {} and reject {} requests for event with ID {}", confirmedRequests.size(), rejectedRequests.size(), eventID);
        return OutcomeGroupedRequestsDTO.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }

    private void checkExistenceUserAndEvent(long userID, long eventID, Optional<User> user, Optional<Event> event) {
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with ID " + userID + " not presented");
        }
        if (event.isEmpty()) {
            throw new EventNotFoundException("Event with ID " + eventID + " not presented");
        }
    }

    private void checkExistenceAndInitiator(long userID, long eventID, Optional<User> user, Optional<Event> event) {
        checkExistenceUserAndEvent(userID, eventID, user, event);
        if (event.get().getInitiator().getId() != user.get().getId()) {
            throw new IncorrectOwnerOfEventException("User with ID " + userID + " not initiator of event with ID " + eventID);
        }
    }
}
