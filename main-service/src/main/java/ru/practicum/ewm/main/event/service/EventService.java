package ru.practicum.ewm.main.event.service;

import ru.practicum.ewm.main.event.dto.EventSort;
import ru.practicum.ewm.main.event.dto.IncomeCreateEventDTO;
import ru.practicum.ewm.main.event.dto.IncomePatchEventDTO;
import ru.practicum.ewm.main.event.dto.OutcomeEventFullDTO;
import ru.practicum.ewm.main.event.dto.OutcomeEventShortDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    OutcomeEventFullDTO addEvent(long userID, IncomeCreateEventDTO dto);

    OutcomeEventFullDTO patchEventByInitiator(long userID, long eventID, IncomePatchEventDTO dto);

    OutcomeEventFullDTO patchEventByAdmin(long eventID, IncomePatchEventDTO dto);

    OutcomeEventFullDTO getPublicEventByID(long eventID);

    OutcomeEventFullDTO getEventOfInitiatorByID(long userID, long eventID);

    List<OutcomeEventShortDTO> getEventsOfInitiator(long userID, int from, int size);

    List<OutcomeEventShortDTO> publicEventSearch(
            String text, List<Integer> categories, boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
            boolean onlyAvailable, EventSort sort, int from, int size
    );

    List<OutcomeEventFullDTO> adminEventSearch(
            List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart,
            LocalDateTime rangeEnd, int from, int size
    );
}
