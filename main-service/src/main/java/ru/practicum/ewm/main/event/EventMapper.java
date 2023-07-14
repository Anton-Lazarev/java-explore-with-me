package ru.practicum.ewm.main.event;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.main.category.Category;
import ru.practicum.ewm.main.category.CategoryMapper;
import ru.practicum.ewm.main.event.dto.IncomeCreateEventDTO;
import ru.practicum.ewm.main.event.dto.Location;
import ru.practicum.ewm.main.event.dto.OutcomeEventFullDTO;
import ru.practicum.ewm.main.event.dto.OutcomeEventShortDTO;
import ru.practicum.ewm.main.user.User;
import ru.practicum.ewm.main.user.UserMapper;

@UtilityClass
public class EventMapper {
    public Event incomeEventCreationDtoToEvent(User initiator, Category category, IncomeCreateEventDTO dto) {
        return Event.builder()
                .title(dto.getTitle())
                .annotation(dto.getAnnotation())
                .category(category)
                .description(dto.getDescription())
                .initiator(initiator)
                .eventDate(dto.getEventDate())
                .longitude(dto.getLocation().getLon())
                .latitude(dto.getLocation().getLat())
                .paid(dto.isPaid())
                .membersLimit(dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration())
                .build();
    }

    public OutcomeEventFullDTO eventToFullEventDTO(Event event, long confirmedRequests, long views) {
        return OutcomeEventFullDTO.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .category(CategoryMapper.categoryToCategoryDTO(event.getCategory()))
                .initiator(UserMapper.userToShortDTO(event.getInitiator()))
                .eventDate(event.getEventDate())
                .location(Location.builder().lon(event.getLongitude()).lat(event.getLatitude()).build())
                .paid(event.isPaid())
                .participantLimit(event.getMembersLimit())
                .requestModeration(event.isRequestModeration())
                .confirmedRequests(confirmedRequests)
                .createdOn(event.getCreateDate())
                .state(event.getState().name())
                .publishedOn(event.getPublicationDate())
                .views(views)
                .build();
    }

    public OutcomeEventShortDTO eventToShortEventDTO(Event event, long confirmedRequests, long views) {
        return OutcomeEventShortDTO.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToCategoryDTO(event.getCategory()))
                .initiator(UserMapper.userToShortDTO(event.getInitiator()))
                .eventDate(event.getEventDate())
                .paid(event.isPaid())
                .confirmedRequests(confirmedRequests)
                .views(views)
                .build();
    }
}
