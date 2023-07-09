package ru.practicum.ewm.main.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.main.category.Category;
import ru.practicum.ewm.main.event.dto.EventStatus;
import ru.practicum.ewm.main.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "events", schema = "public")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String annotation;
    private String description;
    private boolean paid;

    @Column(name = "start_date")
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id")
    @ToString.Exclude
    private User initiator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private Category category;

    @Column(name = "members_limit")
    private int membersLimit;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Column(name = "location_lat")
    private float latitude;

    @Column(name = "location_lon")
    private float longitude;

    @Enumerated(EnumType.STRING)
    private EventStatus state;

    @Column(name = "created")
    private LocalDateTime createDate;

    @Column(name = "publication_date")
    private LocalDateTime publicationDate;
}
