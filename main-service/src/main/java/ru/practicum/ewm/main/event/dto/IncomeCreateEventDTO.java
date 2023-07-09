package ru.practicum.ewm.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncomeCreateEventDTO {
    @NotBlank(message = "Event title can't be blank, empty or null")
    @Size(min = 3, max = 120, message = "Size of title should be between 3 and 120")
    private String title;
    @NotBlank(message = "Event annotation can't be blank, empty or null")
    @Size(min = 20, max = 2000, message = "Size of annotation should be between 20 and 2000")
    private String annotation;
    @NotNull(message = "ID of category can't be null")
    private Long category;
    @NotBlank(message = "Event description can't be blank, empty or null")
    @Size(min = 20, max = 7000, message = "Size of description should be between 20 and 7000")
    private String description;
    @NotNull(message = "Event date can't be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull(message = "Event location can't be null")
    private Location location;

    private boolean paid;
    private int participantLimit;
    private Boolean requestModeration;
}
