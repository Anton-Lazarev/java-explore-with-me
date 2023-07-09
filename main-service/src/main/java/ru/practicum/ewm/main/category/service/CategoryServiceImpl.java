package ru.practicum.ewm.main.category.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.Paginator;
import ru.practicum.ewm.main.category.Category;
import ru.practicum.ewm.main.category.CategoryDTO;
import ru.practicum.ewm.main.category.CategoryMapper;
import ru.practicum.ewm.main.category.CategoryRepository;
import ru.practicum.ewm.main.exceptions.CategoryNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    @Override
    public CategoryDTO addCategory(CategoryDTO dto) {
        Category newCategory = repository.save(CategoryMapper.categoryDtoToCategory(dto));
        log.info("Create new category with ID: {}, name: {}", newCategory.getId(), newCategory.getName());
        return CategoryMapper.categoryToCategoryDTO(newCategory);
    }

    @Override
    public CategoryDTO patchCategory(CategoryDTO dto) {
        Category patchedCategory = repository.save(CategoryMapper.categoryDtoToCategory(dto));
        log.info("Patched category with ID: {}, new name: {}", patchedCategory.getId(), patchedCategory.getName());
        return CategoryMapper.categoryToCategoryDTO(patchedCategory);
    }

    @Override
    public void deleteCategoryByID(long id) {
        if (!repository.existsById(id)) {
            throw new CategoryNotFoundException("Category with ID " + id + " not presented");
        }
        log.info("Category with ID {} deleted", id);
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategoryByID(long id) {
        Optional<Category> category = repository.findById(id);
        if (category.isEmpty()) {
            throw new CategoryNotFoundException("Category with ID " + id + " not presented");
        }
        log.info("Got category with ID {}, name {}", category.get().getId(), category.get().getId());
        return CategoryMapper.categoryToCategoryDTO(category.get());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getPageOfCategories(int from, int size) {
        List<CategoryDTO> dtos = repository.findAll(new Paginator(from, size))
                .stream().map(CategoryMapper::categoryToCategoryDTO).collect(Collectors.toList());
        log.info("Got list of categories with size {}", dtos.size());
        return dtos;
    }
}
