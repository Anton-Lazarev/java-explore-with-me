package ru.practicum.ewm.main.likes;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LikeMapper {
    public LikeDTO likeToLikeDTO(Like like) {
        return LikeDTO.builder()
                .id(like.getId())
                .userID(like.getUser().getId())
                .eventID(like.getEvent().getId())
                .isLike(like.isLike())
                .created(like.getCreated())
                .build();
    }
}
