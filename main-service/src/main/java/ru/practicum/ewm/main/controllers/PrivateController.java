package ru.practicum.ewm.main.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.main.event.dto.IncomeCreateEventDTO;
import ru.practicum.ewm.main.event.dto.IncomePatchEventDTO;
import ru.practicum.ewm.main.event.dto.OutcomeEventFullDTO;
import ru.practicum.ewm.main.event.dto.OutcomeEventShortDTO;
import ru.practicum.ewm.main.event.service.EventService;
import ru.practicum.ewm.main.requests.dto.ChangeStatusRequestsDTO;
import ru.practicum.ewm.main.requests.dto.EventRequestDTO;
import ru.practicum.ewm.main.requests.dto.OutcomeGroupedRequestsDTO;
import ru.practicum.ewm.main.requests.service.EventRequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public class PrivateController {
    private final EventService eventService;
    private final EventRequestService requestService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userID}/events")
    public OutcomeEventFullDTO saveEvent(@PathVariable long userID, @Valid @RequestBody IncomeCreateEventDTO dto) {
        log.info("EWM main service: POST to /users/{}/events with {}", userID, dto.toString());
        return eventService.addEvent(userID, dto);
    }

    @PatchMapping("/{userID}/events/{eventID}")
    public OutcomeEventFullDTO updateEventByOwner(@PathVariable long userID,
                                                  @PathVariable long eventID,
                                                  @RequestBody @Valid IncomePatchEventDTO dto) {
        log.info("EWM main service: PATCH to /users/{}/events/{} with {}", userID, eventID, dto.toString());
        return eventService.patchEventByInitiator(userID, eventID, dto);
    }

    @GetMapping("/{userID}/events")
    public List<OutcomeEventShortDTO> findEventsOfInitiator(@PathVariable long userID,
                                                            @RequestParam(defaultValue = "0") int from,
                                                            @RequestParam(defaultValue = "10") int size) {
        PageValidator.validate(from, size);
        log.info("EWM main service: GET to /users/{}/events with from {} and size {}", userID, from, size);
        return eventService.getEventsOfInitiator(userID, from, size);
    }

    @GetMapping("/{userID}/events/{eventID}")
    public OutcomeEventFullDTO findEventByID(@PathVariable long userID, @PathVariable long eventID) {
        log.info("EWM main service: GET to /users/{}/events/{}", userID, eventID);
        return eventService.getEventOfInitiatorByID(userID, eventID);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userID}/requests")
    public EventRequestDTO saveRequestToEvent(@PathVariable long userID,
                                              @RequestParam(name = "eventId") long eventID) {
        log.info("EWM main service: POST to /users/{}/requests?eventId={}", userID, eventID);
        return requestService.addEventRequest(userID, eventID);
    }

    @PatchMapping("/{userID}/requests/{requestID}/cancel")
    public EventRequestDTO cancelRequestByRequester(@PathVariable long userID, @PathVariable long requestID) {
        log.info("EWM main service: PATCH to /users/{}/requests/{}/cancel", userID, requestID);
        return requestService.cancelRequestByRequester(userID, requestID);
    }

    @PatchMapping("{userID}/events/{eventID}/requests")
    public OutcomeGroupedRequestsDTO changeStatusesOfRequestsByInitiator(@PathVariable long userID,
                                                                         @PathVariable long eventID,
                                                                         @Valid @RequestBody ChangeStatusRequestsDTO dto) {
        log.info("EWM main service: PATCH to /users/{}/events/{}/requests with {}", userID, eventID, dto.toString());
        return requestService.changeStatusOfRequestsByEventInitiator(userID, eventID, dto);
    }

    @GetMapping("/{userID}/requests")
    public List<EventRequestDTO> findRequestsOfUser(@PathVariable long userID) {
        log.info("EWM main service: GET to /users/{}/requests", userID);
        return requestService.getAllRequestsOfRequester(userID);
    }

    @GetMapping("{userID}/events/{eventID}/requests")
    public List<EventRequestDTO> findRequestsForEventOfUser(@PathVariable long userID, @PathVariable long eventID) {
        log.info("EWM main service: GET to /users/{}/events/{}/requests", userID, eventID);
        return requestService.getAllRequestsToEventByEventInitiator(userID, eventID);
    }
}
