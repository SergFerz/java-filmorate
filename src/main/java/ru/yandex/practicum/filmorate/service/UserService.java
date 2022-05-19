package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Long idUser1, Long idUser2) {
        List<User> users = (List<User>) userStorage.findAll();
        User user1 = users.get(Math.toIntExact(idUser1));
        Set<Long> friendsUser1 = user1.getFriends();
        friendsUser1.add(idUser2);
        user1.setFriends(friendsUser1);
        userStorage.update(user1);

        User user2 = users.get(Math.toIntExact(idUser2));
        Set<Long> friendsUser2 = user2.getFriends();
        friendsUser2.add(idUser1);
        user2.setFriends(friendsUser2);
        userStorage.update(user2);
    }

    public void deleteFriend(Long idUser1, Long idUser2){
        List<User> users = (List<User>) userStorage.findAll();
        User user1 = users.get(Math.toIntExact(idUser1));
        Set<Long> friendsUser1 = user1.getFriends();
        friendsUser1.remove(idUser2);
        user1.setFriends(friendsUser1);
        userStorage.update(user1);

        User user2 = users.get(Math.toIntExact(idUser2));
        Set<Long> friendsUser2 = user2.getFriends();
        friendsUser2.remove(idUser1);
        user2.setFriends(friendsUser2);
        userStorage.update(user2);
    }

    public List<User> getAllFriends(Long id) {
        List<User> users = (List<User>) userStorage.findAll();
        User user = users.get(Math.toIntExact(id));
        Set<Long> friendsUserId = user.getFriends();
        List<User> friendsList = new ArrayList<>();
        for (Long i : friendsUserId) {
            friendsList.add(users.get(Math.toIntExact(i)));
        }
       return friendsList;
    }

    public List<User> getCommonFriends(Long idUser1, Long idUser2) {
        List<User> users = (List<User>) userStorage.findAll();
        User user1 = users.get(Math.toIntExact(idUser1));
        User user2 = users.get(Math.toIntExact(idUser2));
        List<User> commonFriends = new ArrayList<>();

        Collection<Long> commonFriendsId = (Collection<Long>) user1.getFriends().stream()
                .takeWhile(item1 -> user2.getFriends().stream().allMatch(item2 -> item1.equals(item2)));

        for (Long i : commonFriendsId) {
            commonFriends.add(users.get(Math.toIntExact(i)));
        }
        return commonFriends;
    }
}
