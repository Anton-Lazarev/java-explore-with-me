package ru.practicum.ewm.main.requests.service;

import ru.practicum.ewm.main.requests.dto.ChangeStatusRequestsDTO;
import ru.practicum.ewm.main.requests.dto.EventRequestDTO;
import ru.practicum.ewm.main.requests.dto.OutcomeGroupedRequestsDTO;

import java.util.List;

public interface EventRequestService {
    EventRequestDTO addEventRequest(long userID, long eventID);

    EventRequestDTO cancelRequestByRequester(long userID, long reqID);

    List<EventRequestDTO> getAllRequestsOfRequester(long userID);

    List<EventRequestDTO> getAllRequestsToEventByEventInitiator(long userID, long eventID);

    OutcomeGroupedRequestsDTO changeStatusOfRequestsByEventInitiator(long userID, long eventID, ChangeStatusRequestsDTO dto);
}
