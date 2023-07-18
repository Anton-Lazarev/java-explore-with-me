package ru.practicum.ewm.main.event.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CollectedEventExtraFields {
    private long requests;
    private long views;
    private long likes;
    private long dislikes;
}
