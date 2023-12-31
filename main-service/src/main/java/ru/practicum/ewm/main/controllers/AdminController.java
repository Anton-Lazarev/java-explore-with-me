package ru.practicum.ewm.main.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.main.category.CategoryDTO;
import ru.practicum.ewm.main.category.service.CategoryService;
import ru.practicum.ewm.main.compilations.dto.IncomeCompilationDTO;
import ru.practicum.ewm.main.compilations.dto.IncomePatchCompilationDTO;
import ru.practicum.ewm.main.compilations.dto.OutcomeCompilationDTO;
import ru.practicum.ewm.main.compilations.service.CompilationService;
import ru.practicum.ewm.main.event.dto.IncomePatchEventDTO;
import ru.practicum.ewm.main.event.dto.OutcomeEventFullDTO;
import ru.practicum.ewm.main.event.dto.SearchAdminParams;
import ru.practicum.ewm.main.event.service.EventService;
import ru.practicum.ewm.main.user.dto.UserDTO;
import ru.practicum.ewm.main.user.dto.UserWithLikesDTO;
import ru.practicum.ewm.main.user.service.UserService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin")
@AllArgsConstructor
@Slf4j
public class AdminController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    public UserDTO saveUser(@Valid @RequestBody UserDTO userDTO) {
        log.info("EWM main service: POST to /admin/users with {}", userDTO.toString());
        return userService.addUser(userDTO);
    }

    @GetMapping("/users")
    public List<UserWithLikesDTO> findUsers(@RequestParam(required = false) List<Long> ids,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size) {
        PageValidator.validate(from, size);
        log.info("EWM main service: GET to /admin/users with ids: {}, from: {}, size: {}", ids, from, size);
        return userService.getPageOfUsers(ids, from, size);
    }

    @GetMapping("/users/{userID}")
    public UserWithLikesDTO findUserByID(@PathVariable long userID) {
        log.info("EWM main service: GET to /admin/users/{}", userID);
        return userService.getUserByID(userID);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/users/{userID}")
    public void deleteUser(@PathVariable long userID) {
        log.info("EWM main service: DELETE to /admin/users/{}", userID);
        userService.deleteUserByID(userID);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/categories")
    public CategoryDTO saveCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        log.info("EWM main service: POST to /admin/categories with {}", categoryDTO.toString());
        return categoryService.addCategory(categoryDTO);
    }

    @PatchMapping("/categories/{catID}")
    public CategoryDTO patchCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                     @PathVariable long catID) {
        log.info("EWM main service: PATCH to /admin/categories/{} with {}", catID, categoryDTO.toString());
        categoryDTO.setId(catID);
        return categoryService.patchCategory(categoryDTO);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/categories/{catID}")
    public void deleteCategory(@PathVariable long catID) {
        log.info("EWM main service: DELETE to /admin/categories/{}", catID);
        categoryService.deleteCategoryByID(catID);
    }

    @PatchMapping("/events/{eventID}")
    public OutcomeEventFullDTO patchEvent(@PathVariable long eventID,
                                          @Valid @RequestBody IncomePatchEventDTO dto) {
        log.info("EWM main service: PATCH to /admin/events/{} with {}", eventID, dto.toString());
        return eventService.patchEventByAdmin(eventID, dto);
    }

    @GetMapping("/events")
    public List<OutcomeEventFullDTO> searchEvents(@RequestParam(required = false) List<Long> users,
                                                  @RequestParam(required = false) List<String> states,
                                                  @RequestParam(required = false) List<Long> categories,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "10") int size) {
        PageValidator.validate(from, size);
        SearchAdminParams params = SearchAdminParams.builder()
                .userIDs(users)
                .states(states)
                .categoryIDs(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .build();
        log.info("EWM main service: GET to /admin/events/ with from: {}, size:{} and params: {}", from, size, params.toString());
        return eventService.adminEventSearch(params, from, size);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/compilations")
    public OutcomeCompilationDTO saveCompilation(@Valid @RequestBody IncomeCompilationDTO compilationDTO) {
        log.info("EWM main service: POST to /admin/compilations with {}", compilationDTO.toString());
        return compilationService.addCompilation(compilationDTO);
    }

    @PatchMapping("/compilations/{compID}")
    public OutcomeCompilationDTO patchCompilation(@PathVariable long compID,
                                                  @Valid @RequestBody IncomePatchCompilationDTO compilationDTO) {
        log.info("EWM main service: PATCH to /admin/compilations/{} with {}", compID, compilationDTO.toString());
        return compilationService.updateCompilation(compID, compilationDTO);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/compilations/{compID}")
    public void deleteCompilation(@PathVariable long compID) {
        log.info("EWM main service: DELETE to /admin/compilations/{}", compID);
        compilationService.deleteCompilationByID(compID);
    }
}
