package ru.practicum.ewm.main.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserShortDTO {
    private long id;
    private String name;
}
