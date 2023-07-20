package ru.practicum.ewm.main.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.main.StatisticForEvents;
import ru.practicum.ewm.main.category.CategoryDTO;
import ru.practicum.ewm.main.category.service.CategoryService;
import ru.practicum.ewm.main.compilations.dto.OutcomeCompilationDTO;
import ru.practicum.ewm.main.compilations.service.CompilationService;
import ru.practicum.ewm.main.event.dto.OutcomeEventFullDTO;
import ru.practicum.ewm.main.event.dto.OutcomeEventShortDTO;
import ru.practicum.ewm.main.event.dto.SearchPublicParams;
import ru.practicum.ewm.main.event.dto.enums.EventSort;
import ru.practicum.ewm.main.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/")
@AllArgsConstructor
@Slf4j
public class PublicController {
    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;
    private final StatisticForEvents eventStats;

    @GetMapping("/categories")
    public List<CategoryDTO> findCategories(@RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size) {
        PageValidator.validate(from, size);
        log.info("EWM main service: GET to /categories with from {} and size {}", from, size);
        return categoryService.getPageOfCategories(from, size);
    }

    @GetMapping("/categories/{catID}")
    public CategoryDTO findCategoryByID(@PathVariable long catID) {
        log.info("EWM main service: GET to /categories/{}", catID);
        return categoryService.getCategoryByID(catID);
    }

    @GetMapping("/events/{eventID}")
    public OutcomeEventFullDTO findEventByID(@PathVariable long eventID, HttpServletRequest request) {
        log.info("EWM main service: GET to /events/{}", eventID);
        OutcomeEventFullDTO dto = eventService.getPublicEventByID(eventID);
        eventStats.addHitToStatistic(request.getRequestURI(), "EWM main service", request.getRemoteAddr(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return dto;
    }

    @GetMapping("/events")
    public List<OutcomeEventShortDTO> searchEvents(@RequestParam(required = false) String text,
                                                   @RequestParam(required = false) List<Integer> categories,
                                                   @RequestParam(defaultValue = "false") boolean paid,
                                                   @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                   @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                   @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                                   @RequestParam(required = false) EventSort sort,
                                                   @RequestParam(defaultValue = "0") int from,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   HttpServletRequest request) {
        if (rangeStart != null && rangeEnd != null) {
            DatesValidator.validate(rangeStart, rangeEnd);
        }
        PageValidator.validate(from, size);
        SearchPublicParams params = SearchPublicParams.builder()
                .text(text)
                .categoryIDs(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .build();
        log.info("EWM main service: GET to /events/ with from: {}, size: {} and params: {}", from, size, params.toString());

        List<OutcomeEventShortDTO> dtos = eventService.publicEventSearch(params, from, size);
        eventStats.addHitToStatistic(request.getRequestURI(), "EWM main service", request.getRemoteAddr(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return dtos;
    }

    @GetMapping("/compilations")
    public List<OutcomeCompilationDTO> searchCompilations(@RequestParam(required = false) boolean pinned,
                                                          @RequestParam(defaultValue = "0") int from,
                                                          @RequestParam(defaultValue = "10") int size) {
        log.info("EWM main service: GET to /compilations with pinned: {}, from: {}, size: {}", pinned, from, size);
        PageValidator.validate(from, size);
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/compilations/{compID}")
    public OutcomeCompilationDTO findCompilationByID(@PathVariable long compID) {
        log.info("EWM main service: GET to /compilations/{}", compID);
        return compilationService.getCompilationByID(compID);
    }
}
