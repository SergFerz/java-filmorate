# java-filmorate
Template repository for Filmorate project.

![Diagram](https://github.com/SergFerz/java-filmorate/blob/add-friends-likes/Screenshot_57.png)

+ films - film_genre: у одного фильма может быть много записей в жанрах (по тз)

+ film_genre - genre: один к одному

+ films - rating_mpa: один к одному

+ films - likes: один ко многим (один фильм много лайков от юзеров)

+ users - likes: один ко многим (один юзер много лайков к фильмам)

+ users - friends: один ко многим (один юзер много друзей)

  + причем в таблице friends внешние ключи на таблицу users у обоих полей (user_id, friend_id)
