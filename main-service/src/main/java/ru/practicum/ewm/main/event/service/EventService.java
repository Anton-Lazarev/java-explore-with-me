package ru.practicum.ewm.main.event.service;

import ru.practicum.ewm.main.event.dto.IncomeCreateEventDTO;
import ru.practicum.ewm.main.event.dto.IncomePatchEventDTO;
import ru.practicum.ewm.main.event.dto.OutcomeEventFullDTO;
import ru.practicum.ewm.main.event.dto.OutcomeEventShortDTO;
import ru.practicum.ewm.main.event.dto.SearchAdminParams;
import ru.practicum.ewm.main.event.dto.SearchPublicParams;

import java.util.List;

public interface EventService {
    OutcomeEventFullDTO addEvent(long userID, IncomeCreateEventDTO dto);

    OutcomeEventFullDTO patchEventByInitiator(long userID, long eventID, IncomePatchEventDTO dto);

    OutcomeEventFullDTO patchEventByAdmin(long eventID, IncomePatchEventDTO dto);

    OutcomeEventFullDTO getPublicEventByID(long eventID);

    OutcomeEventFullDTO getEventOfInitiatorByID(long userID, long eventID);

    List<OutcomeEventShortDTO> getEventsOfInitiator(long userID, int from, int size);

    List<OutcomeEventShortDTO> publicEventSearch(SearchPublicParams params, int from, int size);

    List<OutcomeEventFullDTO> adminEventSearch(SearchAdminParams params, int from, int size);
}
