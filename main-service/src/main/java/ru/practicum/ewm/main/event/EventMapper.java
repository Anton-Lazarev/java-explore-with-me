package ru.practicum.ewm.main.event;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.main.category.Category;
import ru.practicum.ewm.main.category.CategoryMapper;
import ru.practicum.ewm.main.event.dto.CollectedEventExtraFields;
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

    public OutcomeEventFullDTO eventToFullEventDTO(Event event, CollectedEventExtraFields extraFields) {
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
                .confirmedRequests(extraFields.getRequests())
                .createdOn(event.getCreateDate())
                .state(event.getState().name())
                .publishedOn(event.getPublicationDate())
                .views(extraFields.getViews())
                .likes(extraFields.getLikes())
                .dislikes(extraFields.getDislikes())
                .build();
    }

    public OutcomeEventShortDTO eventToShortEventDTO(Event event, CollectedEventExtraFields extraFields) {
        return OutcomeEventShortDTO.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToCategoryDTO(event.getCategory()))
                .initiator(UserMapper.userToShortDTO(event.getInitiator()))
                .eventDate(event.getEventDate())
                .paid(event.isPaid())
                .confirmedRequests(extraFields.getRequests())
                .views(extraFields.getViews())
                .likes(extraFields.getLikes())
                .dislikes(extraFields.getDislikes())
                .build();
    }
}
