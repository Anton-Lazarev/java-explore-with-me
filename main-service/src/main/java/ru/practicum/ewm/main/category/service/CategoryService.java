package ru.practicum.ewm.main.category.service;

import ru.practicum.ewm.main.category.CategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO addCategory(CategoryDTO dto);

    CategoryDTO patchCategory(CategoryDTO dto);

    void deleteCategoryByID(long id);

    CategoryDTO getCategoryByID(long id);

    List<CategoryDTO> getPageOfCategories(int from, int size);
}
