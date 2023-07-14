package ru.practicum.ewm.main.compilations.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.Paginator;
import ru.practicum.ewm.main.StatisticForEvents;
import ru.practicum.ewm.main.compilations.Compilation;
import ru.practicum.ewm.main.compilations.CompilationMapper;
import ru.practicum.ewm.main.compilations.CompilationRepository;
import ru.practicum.ewm.main.compilations.dto.IncomeCompilationDTO;
import ru.practicum.ewm.main.compilations.dto.IncomePatchCompilationDTO;
import ru.practicum.ewm.main.compilations.dto.OutcomeCompilationDTO;
import ru.practicum.ewm.main.event.Event;
import ru.practicum.ewm.main.event.EventMapper;
import ru.practicum.ewm.main.event.EventRepository;
import ru.practicum.ewm.main.event.dto.OutcomeEventShortDTO;
import ru.practicum.ewm.main.exceptions.CompilationNotFoundException;
import ru.practicum.ewm.main.requests.EventRequestRepository;
import ru.practicum.ewm.main.requests.dto.EventRequestStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class CompilationServiceImpl implements CompilationService {
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;
    private final EventRequestRepository requestRepository;
    private final StatisticForEvents statsForEvents;

    @Override
    public OutcomeCompilationDTO addCompilation(IncomeCompilationDTO dto) {
        Compilation newCompilation = CompilationMapper.incomeDtoToCompilation(dto);
        if (dto.getPinned() == null) {
            newCompilation.setPinned(false);
        }
        List<Event> eventsToCompilation;
        if (dto.getEvents() == null || dto.getEvents().isEmpty()) {
            eventsToCompilation = Collections.emptyList();
        } else {
            eventsToCompilation = eventRepository.findAllById(dto.getEvents());
        }
        newCompilation.setEvents(eventsToCompilation);
        compilationRepository.save(newCompilation);
        log.info("Save new compilation with ID {} and title {}", newCompilation.getId(), newCompilation.getTitle());
        return CompilationMapper.compilationToOutcomeDTO(newCompilation, prepareEvents(newCompilation.getEvents()));
    }

    @Override
    public OutcomeCompilationDTO updateCompilation(long compID, IncomePatchCompilationDTO dto) {
        Optional<Compilation> compilation = compilationRepository.findById(compID);
        if (compilation.isEmpty()) {
            throw new CompilationNotFoundException("Compilation with ID " + compID + " not presented");
        }
        if (dto.getEvents() != null) {
            compilation.get().setEvents(eventRepository.findAllById(dto.getEvents()));
        }
        if (dto.getPinned() != null) {
            compilation.get().setPinned(dto.getPinned());
        }
        if (dto.getTitle() != null) {
            compilation.get().setTitle(dto.getTitle());
        }
        compilationRepository.save(compilation.get());
        log.info("Updating compilation with ID {}", compID);
        return CompilationMapper.compilationToOutcomeDTO(compilation.get(), prepareEvents(compilation.get().getEvents()));
    }

    @Override
    public void deleteCompilationByID(long compID) {
        if (!compilationRepository.existsById(compID)) {
            throw new CompilationNotFoundException("Compilation with ID " + compID + " not presented");
        }
        compilationRepository.deleteById(compID);
        log.info("Compilation with ID {} deleted", compID);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OutcomeCompilationDTO> getCompilations(boolean pinned, int from, int size) {
        List<Compilation> compilations;
        if (pinned) {
            compilations = compilationRepository.findAllByPinned(true, new Paginator(from, size));
        } else {
            compilations = compilationRepository.findAll(new Paginator(from, size)).toList();
        }
        List<OutcomeCompilationDTO> dtos = new ArrayList<>();
        for (Compilation currCompilation : compilations) {
            dtos.add(CompilationMapper.compilationToOutcomeDTO(currCompilation,
                    prepareEvents(currCompilation.getEvents())));
        }
        log.info("Got list with size {} of compilations for search", dtos.size());
        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public OutcomeCompilationDTO getCompilationByID(long compID) {
        Optional<Compilation> compilation = compilationRepository.findById(compID);
        if (compilation.isEmpty()) {
            throw new CompilationNotFoundException("Compilation with ID " + compID + " not presented");
        }
        log.info("Got single compilation with ID {} and title {}", compID, compilation.get().getTitle());
        return CompilationMapper.compilationToOutcomeDTO(compilation.get(), prepareEvents(compilation.get().getEvents()));
    }

    private List<OutcomeEventShortDTO> prepareEvents(List<Event> events) {
        List<OutcomeEventShortDTO> eventShortDTOs = new ArrayList<>();
        if (events.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> eventIDs = events.stream().map(Event::getId).collect(Collectors.toList());
        Map<Long, Long> stats = statsForEvents.getHitsForEvents(eventIDs);
        for (Event event : events) {
            eventShortDTOs.add(EventMapper.eventToShortEventDTO(event,
                    requestRepository.countAllByEventIdAndStatus(event.getId(), EventRequestStatus.CONFIRMED),
                    stats.get(event.getId())));
        }
        return eventShortDTOs;
    }
}
