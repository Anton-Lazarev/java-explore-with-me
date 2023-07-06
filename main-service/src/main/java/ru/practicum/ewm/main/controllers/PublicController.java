package ru.practicum.ewm.main.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.main.category.CategoryDTO;
import ru.practicum.ewm.main.category.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/")
@AllArgsConstructor
@Slf4j
public class PublicController {
    private final CategoryService categoryService;

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
}
