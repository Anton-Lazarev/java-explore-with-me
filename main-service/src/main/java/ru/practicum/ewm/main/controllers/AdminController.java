package ru.practicum.ewm.main.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.main.user.UserDTO;
import ru.practicum.ewm.main.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin")
@AllArgsConstructor
@Slf4j
public class AdminController {
    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    public UserDTO saveUser(@Valid @RequestBody UserDTO userDTO) {
        log.info("EWM main service: POST to /admin/users with {}", userDTO.toString());
        return userService.addUser(userDTO);
    }

    @GetMapping("/users")
    public List<UserDTO> findUsers(@RequestParam(required = false) List<Long> ids,
                                   @RequestParam(defaultValue = "0") int from,
                                   @RequestParam(defaultValue = "10") int size) {
        PageValidator.validate(from, size);
        log.info("EWM main service: GET to /admin/users with ids: {}, from: {}, size: {}", ids, from, size);
        return userService.getPageOfUsers(ids, from, size);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/users/{userID}")
    public void deleteUser(@PathVariable long userID) {
        log.info("EWM main service: DELETE to /admin/users with id: {}", userID);
        userService.deleteUserByID(userID);
    }
}
