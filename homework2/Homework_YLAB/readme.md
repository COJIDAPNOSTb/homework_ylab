# Автосалон - Консольное Приложение

Необходимо обновить сервис, который был разработан в первом задании согласно новым требованиям и ограничениям

## Функциональные возможности

- Репозитории теперь пишут все сущности в БД PostgreSQL
- Идентификаторы при сохранении в БД выдаются через sequence
- DDL-скрипты на создание таблиц и скрипты на предзаполнение таблиц выполняются только инструментом миграции Liquibase
- Скрипты миграции Luiqbase написаны в нотации XML или YAML
- Служебные таблицы находятся в отдельной схеме (liquibase)
- В тестах необходимо используются test-containers
- В приложении есть docker-compose.yml, в котором прописаны инструкции для развертывания postgre в докере.
- Приложение должно поддерживает конфиг-файлы. 

## Технологии

- Java
- JUnit 5
- Mockito
- AssertJ
- PostgreSQL
- Liquibase
- Docker
- Testcontainers
- Logback

## Структура проекта
src/main/java - исходный код приложения
src/main/resources/db/changelog - Liquibase миграции
docker-compose.yml - настройки Docker для PostgreSQL
src/main/resources/config.properties - содержит параметры подключения к базе данных и другие настройки приложения.
## Запуск
 В корневой папки проекта:
 docker-compose up -d запуск докера
 docker exec -it YLABHW psql -U testuser -d testdb подключение к базе данных
 mvn liquibase:update запуск миграции
 Main.java основной класс работы



