package ru.practicum.ewm.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.main.category.CategoryDTO;
import ru.practicum.ewm.main.user.dto.UserShortDTO;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutcomeEventShortDTO {
    private long id;
    private String title;
    private String annotation;
    private CategoryDTO category;
    private UserShortDTO initiator;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private boolean paid;
    private long confirmedRequests;
    private long views;
}
