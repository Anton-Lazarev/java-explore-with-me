package ru.practicum.ewm.main.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserWithLikesDTO {
    private long id;
    private String name;
    private String email;
    private long likes;
    private long dislikes;
}
