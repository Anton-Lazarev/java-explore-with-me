# Explore-With-Me app
This is Maven multi-module project include:

## Statistic service:
Main aim this service: collect and provide statistic of accessing to event endpoints.
Additionally, for granted statistic to main service created HTTP client for simple cooperation between them.

API specification for statistic service [here](/ewm-main-service-spec.json).

POSTMAN tests for statistic service [here](https://github.com/yandex-praktikum/java-explore-with-me/blob/main_svc/postman/ewm-stat-service.json).

## Main service:
Main functionality of this service:
* Add / modify users;
* Add / modify / delete event categories;
* Add / modify / moderate events;
* Add / cancel / moderate requests to participation in events from another users;
* Add / modify / delete compilations of events.

Additional functionality:
* Add likes / dislikes;
* Show rating of events and their initiators.

API specification for main service [here](/ewm-main-service-spec.json).

DB scheme for main service [here](/assets/db-scheme.png).

POSTMAN tests for main service [here](https://github.com/yandex-praktikum/java-explore-with-me/blob/main_svc/postman/ewm-main-service.json).
***
### Stack:
For both services used Java 11, Spring Boot 2.7.8, Hibernate, PostgreSQL.

Both modules collected in two linked docker services via [Docker](/docker-compose.yml) which contains four containers.
***
### Short future plans:
Add new functionality like:
* Comments to events;
* Subscribes to another users.