package ru.practicum.ewm.main.likes.service;

import ru.practicum.ewm.main.likes.LikeDTO;

public interface LikeService {
    LikeDTO addLike(long userID, long eventID, boolean isLike);

    void deleteLike(long userID, long eventID);
}
