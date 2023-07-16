package ru.practicum.ewm.main.category;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class CategoryDTO {
    private long id;
    @NotBlank(message = "Name of category can't be blank, empty or null")
    @Size(min = 1, max = 50, message = "Size of name should be between 1 and 50")
    private String name;
}
