package ru.practicum.ewm.main.event.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.Paginator;
import ru.practicum.ewm.main.category.Category;
import ru.practicum.ewm.main.category.CategoryRepository;
import ru.practicum.ewm.main.event.ConflictStatusException;
import ru.practicum.ewm.main.event.Event;
import ru.practicum.ewm.main.event.EventMapper;
import ru.practicum.ewm.main.event.EventRepository;
import ru.practicum.ewm.main.event.dto.EventStatus;
import ru.practicum.ewm.main.event.dto.EventStatusActions;
import ru.practicum.ewm.main.event.dto.IncomeCreateEventDTO;
import ru.practicum.ewm.main.event.dto.IncomePatchEventDTO;
import ru.practicum.ewm.main.event.dto.OutcomeEventFullDTO;
import ru.practicum.ewm.main.event.dto.OutcomeEventShortDTO;
import ru.practicum.ewm.main.exceptions.CategoryNotFoundException;
import ru.practicum.ewm.main.exceptions.EventDateException;
import ru.practicum.ewm.main.exceptions.EventNotFoundException;
import ru.practicum.ewm.main.exceptions.IncorrectOwnerOfEventException;
import ru.practicum.ewm.main.exceptions.UserNotFoundException;
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
public class EventServiceImpl implements EventService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public OutcomeEventFullDTO addEvent(long userID, IncomeCreateEventDTO dto) {
        Optional<User> initiator = userRepository.findById(userID);
        Optional<Category> category = categoryRepository.findById(dto.getCategory());
        if (initiator.isEmpty()) {
            throw new UserNotFoundException("User with ID " + userID + " not presented");
        }
        if (category.isEmpty()) {
            throw new CategoryNotFoundException("Category with ID " + dto.getCategory() + " not presented");
        }
        if (dto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new EventDateException("Event should start after " + LocalDateTime.now().plusHours(2));
        }
        if (dto.getRequestModeration() == null) {
            dto.setRequestModeration(true);
        }
        Event newEvent = EventMapper.incomeEventCreationDtoToEvent(initiator.get(), category.get(), dto);
        newEvent.setState(EventStatus.PENDING);
        newEvent.setCreateDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        eventRepository.save(newEvent);
        log.info("Created new event with ID: {}, initiator: {}, category: {}", newEvent.getId(), initiator.get().getName(), category.get().getName());
        return EventMapper.eventToFullEventDTO(newEvent, 0, 0);
    }

    @Override
    public OutcomeEventFullDTO patchEventByInitiator(long userID, long eventID, IncomePatchEventDTO dto) {
        Optional<User> user = userRepository.findById(userID);
        Optional<Event> event = eventRepository.findById(eventID);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with ID " + userID + " not presented");
        }
        if (event.isEmpty()) {
            throw new EventNotFoundException("Event with ID " + eventID + " not presented");
        }
        if (event.get().getInitiator().getId() != user.get().getId()) {
            throw new IncorrectOwnerOfEventException("User with ID " + userID + " not owner of event with ID " + eventID);
        }
        if (event.get().getState() == EventStatus.PUBLISHED) {
            throw new ConflictStatusException("Event with ID " + eventID + " already published and closed for changes");
        }
        if (event.get().getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new EventDateException("Event with ID " + eventID + " can't update because of soon start of it");
        }

        updateEventFields(event.get(), dto);
        if (dto.getStateAction() != null && dto.getStateAction().equals(EventStatusActions.CANCEL_REVIEW)) {
            event.get().setState(EventStatus.CANCELED);
        } else {
            event.get().setState(EventStatus.PENDING);
        }
        eventRepository.save(event.get());
        log.info("Initiator update event with ID {} patched and got status {}", eventID, event.get().getState().name());
        //todo реализовать сборку FullEventDTO с views и requests и убрать 0-0
        return EventMapper.eventToFullEventDTO(event.get(), 0, 0);
    }

    @Override
    public OutcomeEventFullDTO patchEventByAdmin(long eventID, IncomePatchEventDTO dto) {
        Optional<Event> event = eventRepository.findById(eventID);
        if (event.isEmpty()) {
            throw new EventNotFoundException("Event with ID " + eventID + " not presented");
        }
        if (event.get().getState() != EventStatus.PENDING) {
            throw new ConflictStatusException("Event with ID " + eventID + " should be with PENDING status");
        }
        if (event.get().getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new EventDateException("Event with ID " + eventID + " can't update because of soon start of it");
        }

        updateEventFields(event.get(), dto);
        if (dto.getStateAction() != null && dto.getStateAction().equals(EventStatusActions.PUBLISH_EVENT)) {
            event.get().setState(EventStatus.PUBLISHED);
            event.get().setPublicationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        } else {
            event.get().setState(EventStatus.CANCELED);
        }
        eventRepository.save(event.get());
        log.info("Admin update event with ID {} patched and got status {}", eventID, event.get().getState().name());
        //todo реализовать сборку FullEventDTO с views и requests и убрать 0-0
        return EventMapper.eventToFullEventDTO(event.get(), 0, 0);
    }

    @Override
    @Transactional(readOnly = true)
    public OutcomeEventFullDTO getPublicEventByID(long eventID) {
        Optional<Event> event = eventRepository.findById(eventID);
        if (event.isEmpty()) {
            throw new EventNotFoundException("Event with ID " + eventID + " not presented");
        }
        if (event.get().getState() != EventStatus.PUBLISHED) {
            throw new EventNotFoundException("Event with ID " + eventID + " not in status PUBLISHED");
        }
        log.info("Got event with ID: {}, and title: {}", event.get().getId(), event.get().getTitle());
        //todo реализовать отправку статистики в клиент по эндпоинту
        //todo реализовать сборку FullEventDTO с views и requests и убрать 0-0
        return EventMapper.eventToFullEventDTO(event.get(), 0, 0);
    }

    @Override
    @Transactional(readOnly = true)
    public OutcomeEventFullDTO getEventOfInitiatorByID(long userID, long eventID) {
        Optional<Event> event = eventRepository.findById(eventID);
        Optional<User> user = userRepository.findById(userID);
        if (event.isEmpty()) {
            throw new EventNotFoundException("Event with ID " + eventID + " not presented");
        }
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with ID " + userID + " not presented");
        }
        if (event.get().getInitiator().getId() != user.get().getId()) {
            throw new IncorrectOwnerOfEventException("User with ID " + userID + " not owner of event with ID " + eventID);
        }
        log.info("Initiator got event with ID: {}, and title: {}", event.get().getId(), event.get().getTitle());
        //todo реализовать сборку FullEventDTO с views и requests и убрать 0-0
        return EventMapper.eventToFullEventDTO(event.get(), 0, 0);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OutcomeEventShortDTO> getEventsOfInitiator(long userID, int from, int size) {
        if (!userRepository.existsById(userID)) {
            throw new UserNotFoundException("User with ID " + userID + " not presented");
        }
        List<OutcomeEventShortDTO> dtos = new ArrayList<>();
        for (Event event : eventRepository.findAllByInitiatorId(userID, new Paginator(from, size))) {
            //todo реализовать сборку ShortEventDTO с views и requests и убрать 0-0
            dtos.add(EventMapper.eventToShortEventDTO(event, 0, 0));
        }
        log.info("Getting list of events with size {} by their initiator with ID {}", dtos.size(), userID);
        return dtos;
    }

    private void updateEventFields(Event event, IncomePatchEventDTO dto) {
        if (dto.getCategory() != null) {
            Optional<Category> category = categoryRepository.findById(dto.getCategory());
            if (category.isEmpty()) {
                throw new CategoryNotFoundException("Category with ID " + dto.getCategory() + " not presented");
            }
            event.setCategory(category.get());
        }
        if (dto.getTitle() != null) {
            event.setTitle(dto.getTitle());
        }
        if (dto.getAnnotation() != null) {
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getDescription() != null) {
            event.setDescription(dto.getDescription());
        }
        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }
        if (dto.getLocation() != null) {
            event.setLatitude(dto.getLocation().getLat());
            event.setLongitude(dto.getLocation().getLon());
        }
        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }
        if (dto.getParticipantLimit() != null) {
            event.setMembersLimit(dto.getParticipantLimit());
        }
        if (dto.getRequestModeration() != null) {
            event.setRequestModeration(dto.getRequestModeration());
        }
    }
}
