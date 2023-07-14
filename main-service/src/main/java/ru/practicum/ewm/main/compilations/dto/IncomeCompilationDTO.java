package ru.practicum.ewm.main.compilations.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class IncomeCompilationDTO {
    private List<Long> events;
    private Boolean pinned;
    @NotBlank(message = "Title of compilation can't be blank, empty or null")
    @Size(min = 1, max = 50, message = "Size of title should be between 1 and 50")
    private String title;
}
