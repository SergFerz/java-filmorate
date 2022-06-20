MERGE INTO genres (id, name)
VALUES  (1, 'Комедия'),
        (2, 'Драма'),
        (3, 'Мультфильм'),
        (4, 'Приключения'),
        (5, 'Боевик'),
        (6, 'Триллер');

MERGE INTO mpa (id, name)
VALUES (1, 'G'),
       (2, 'PG'),
       (3, 'PG-13'),
       (4, 'R'),
       (5, 'NC-17');

INSERT INTO films (NAME, RELEASEDATE, DESCRIPTION, DURATION, MPA_ID)
VALUES ('name1', '2000-05-05', 'description 1', 110, 2);

INSERT INTO films (NAME, RELEASEDATE, DESCRIPTION, DURATION, MPA_ID)
VALUES ('name2', '2010-01-01', 'description 2', 150, 1);

INSERT INTO users (email, login, name, birthday)
VALUES ( 'user1@gmail.com', 'user1', 'name1',  '2001-01-01');

INSERT INTO users (email, login, name, birthday)
VALUES ( 'user2@gmail.com', 'user2', 'name2',  '2002-02-02');




