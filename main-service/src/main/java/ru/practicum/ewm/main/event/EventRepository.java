package ru.practicum.ewm.main.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiatorId(long userID, Pageable pageable);

    List<Event> findAll(Specification specification, Pageable pageable);
}
