package ru.practicum.ewm.main.requests;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.main.requests.dto.EventRequestStatus;

import java.util.List;
import java.util.Optional;

public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {
    Optional<EventRequest> findByRequesterIdAndEventId(long userID, long eventID);

    long countAllByEventIdAndStatus(long eventID, EventRequestStatus status);

    List<EventRequest> findAllByRequesterId(long id);

    List<EventRequest> findAllByEventId(long id);

    List<EventRequest> findAllByEventIdAndIdIn(long eventID, List<Long> reqIDs);

    Optional<EventRequest> findByRequesterIdAndEventIdAndStatus(long userID, long eventID, EventRequestStatus status);
}
