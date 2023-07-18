INSERT INTO users(name, email)
    VALUES ('firstUser', 'first@user.er');
INSERT INTO users(name, email)
    VALUES ('secondUser', 'second@user.er');

INSERT INTO categories(name)
    VALUES ('main category');

INSERT INTO events(title, annotation, description, start_date, initiator_id, category_id,
    members_limit, request_moderation, paid, location_lat, location_lon, created, state, publication_date)
VALUES ('title for first event', 'annotation for first event', 'description for first event', '2023-08-20 10:00:00',
    1, 1, 200, 'true', 'true', 100, 200, '2023-07-25 11:22:41', 'PUBLISHED', '2023-07-28 14:11:31');

INSERT INTO event_requests(event_id, requester_id, status, created)
    VALUES (1, 2, 'CONFIRMED', '2023-07-30 12:49:58')