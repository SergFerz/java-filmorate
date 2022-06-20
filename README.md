# java-filmorate
Template repository for Filmorate project.

![Diagram](C:\Users\Serg\dev\java-filmorate\DbDiagramFilmorate.png)

+ __films - film_genre:__ у одного фильма может быть много записей в жанрах (по тз)

+ __film_genre - genre:__ один к одному

+ __films - mpa:__ один к одному

+ __films - likes:__ один ко многим (один фильм много лайков от юзеров)

+ __users - likes:__ один ко многим (один юзер много лайков к фильмам)

+ __users - friends:__ один ко многим (один юзер много друзей)

  + причем в таблице friends внешние ключи на таблицу users у обоих полей (user_id, friend_id)
