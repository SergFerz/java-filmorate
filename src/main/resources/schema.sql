DROP TABLE IF EXISTS film_genre, film_director, likes, films, friends, users, mpa, genres, reviews, review_likes, directors;

CREATE TABLE genres
(
    id int PRIMARY KEY,
    name varchar(50)
);

CREATE TABLE mpa
(
    id int PRIMARY KEY,
    name varchar(50)
);

CREATE TABLE directors
(
    id int generated by default as identity primary key,
    name varchar(150)
);

CREATE TABLE users
(
    user_id int generated by default as identity primary key, -- идентификатор целочисленный, автоинкрементный
    email varchar(255),
    login varchar(255),
    name varchar(255),
    birthday date
);

CREATE TABLE friends
(
    user_id int NOT NULL,
    friend_id int NOT NULL,
    status varchar(100),
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (friend_id) REFERENCES users(user_id)
);

CREATE TABLE films
(
    film_id int generated by default as identity primary key, -- идентификатор целочисленный, автоинкрементный
    name varchar(255),
    releaseDate date,
    description varchar(255),
    duration int,
    rate int,
    mpa_id int DEFAULT 5,
    FOREIGN KEY (mpa_id) REFERENCES mpa(id)
);

CREATE TABLE likes
(
    film_id int NOT NULL,
    user_id int NOT NULL,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (film_id) REFERENCES films(film_id)
);

CREATE TABLE film_genre
(
    film_id int NOT NULL,
    id int NOT NULL,
    CONSTRAINT  pk PRIMARY KEY (film_id, id),
    FOREIGN KEY (film_id) REFERENCES films(film_id),
    FOREIGN KEY (id) REFERENCES genres(id)
);

CREATE TABLE film_director
(
    film_id int NOT NULL,
    id int NOT NULL,
    --CONSTRAINT  pk PRIMARY KEY (film_id, id),
    FOREIGN KEY (film_id) REFERENCES films(film_id),
    FOREIGN KEY (id) REFERENCES directors(id) ON DELETE CASCADE
);

CREATE TABLE reviews
(
    review_id int generated by default as identity primary key,
    user_id int NOT NULL,
    film_id int NOT NULL,
    content VARCHAR(200),
    is_positive boolean,
    useful int DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (film_id) REFERENCES films(film_id) ON DELETE CASCADE
);

CREATE TABLE review_likes
(
    review_id int NOT NULL,
    user_id int NOT NULL,
    score int,
    FOREIGN KEY (review_id) REFERENCES reviews(review_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
