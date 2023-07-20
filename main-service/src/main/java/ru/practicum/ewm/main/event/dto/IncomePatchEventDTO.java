package ru.practicum.ewm.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.main.event.dto.enums.EventStatusActions;

import javax.validation.constraints.Future;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomePatchEventDTO {
    @Size(min = 3, max = 120, message = "Size of title should be between 3 and 120")
    private String title;
    @Size(min = 20, max = 2000, message = "Size of annotation should be between 20 and 2000")
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000, message = "Size of description should be between 20 and 7000")
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "Date of event should be in future")
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private EventStatusActions stateAction;
}
