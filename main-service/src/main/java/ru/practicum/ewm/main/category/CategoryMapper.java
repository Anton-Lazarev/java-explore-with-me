package ru.practicum.ewm.main.category;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CategoryMapper {
    public Category categoryDtoToCategory(CategoryDTO dto) {
        return Category.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }

    public CategoryDTO categoryToCategoryDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
