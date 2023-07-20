package ru.practicum.ewm.main.likes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndEventId(long userID, long eventID);

    @Query(value = "select count(l.id) from Like as l where l.event.initiator.id = :UID and l.isLike = :aLike")
    long countLikesByUserIdAndStatus(@Param("UID") long userID, @Param("aLike") boolean isLike);

    @Query(value = "select count(l.id) from Like as l where l.event.id = :EID and l.isLike = :aLike")
    long countLikesByEventIdAndStatus(@Param("EID") long eventID, @Param("aLike") boolean isLike);
}
