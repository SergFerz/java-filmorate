package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private final FilmService filmService;
    private final LikeDao likeDao;

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User create(User user) {
        validateUser(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        getUserById(user.getId());
        validateUser(user);
        return userStorage.update(user);
    }

    public User getUserById(long id) {
        return userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Введено некорректное значение id"));
    }

    public void addFriend(long idUser, long idFriend) {
        getUserById(idUser);
        getUserById(idFriend);
        if (getAllFriends(idFriend).stream().map(User::getId).anyMatch(x -> x.equals(idUser))) {
            userStorage.addFriend(idUser, idFriend, "CONFIRM");
        } else {
            userStorage.addFriend(idUser, idFriend, "UNCONFIRM ");
        }
    }

    public void deleteFriend(long idUser, long idFriend) {
        getUserById(idUser);
        getUserById(idFriend);
        userStorage.deleteFriend(idUser, idFriend);
    }

    public List<User> getAllFriends(long id) {
        return getUserById(id).getFriends().stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long idUser1, long idUser2) {
        User user1 = getUserById(idUser1);
        User user2 = getUserById(idUser2);
        Set<Long> commonFriendsId = new HashSet<>(user1.getFriends());
        commonFriendsId.retainAll(user2.getFriends());
        return commonFriendsId.stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    private User validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            log.debug("Введен некорректный логин");
            throw new ValidationException("Введен некорректный логин");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Введен некорректный день рождения");
            throw new ValidationException("Введен некорректный день рождения");
        }
        if (user.getName().isEmpty() || user.getName() == null) {
            user.setName(user.getLogin());
        }
        return user;
    }

    public void deleteUser(long userId) {
        getUserById(userId);
        userStorage.deleteUser(userId);
    }

    public List<Film> getRecommendations(long id) {
        List<Film> films = new ArrayList<>();
        Set<Long> filmsId = createRecommendations(id);
        if (!filmsId.isEmpty()) {
            for (Long i : filmsId) {
                films.add(filmService.getFilmById(i));
            }
        }
        return films;
    }

    private Set<Long> createRecommendations(long userId) {
        Map<Long, Map<Long, Double>> diffMatrix = new HashMap<>();
        Map<Long, Integer> freq = new HashMap<>();
        Map<Long, Map<Long, Double>> data = likeDao.buildDifferencesMatrix();
        Set<Long> filmSet = new HashSet<>();
        Map<Long, Double> userLikes = data.get(userId);
        for (Map.Entry<Long, Map<Long, Double>> entryData : data.entrySet()) {
            if (entryData.getKey() == userId) {
                continue;
            }
            diffMatrix.put(entryData.getKey(), new HashMap<>());
            for (Map.Entry<Long, Double> entry : entryData.getValue().entrySet()) {
                double diff = userLikes.get(entry.getKey()) * entry.getValue();
                if (diff == 1.0) {
                    if (!freq.containsKey(entryData.getKey())) {
                        freq.put(entryData.getKey(), 0);
                    }
                    int countFreq = freq.get(entryData.getKey());
                    countFreq++;
                    freq.put(entryData.getKey(), countFreq);
                }
                diffMatrix.get(entryData.getKey()).put(entry.getKey(), diff);
            }
        }
        for (Map.Entry<Long, Map<Long, Double>> entryData : data.entrySet()) {
            if (freq.get(entryData.getKey()) == null
                    || freq.get(entryData.getKey()) == 0
                    || entryData.getKey() == userId) {
                continue;
            }
            for (Map.Entry<Long, Double> entry : entryData.getValue().entrySet()) {
                if (data.get(entryData.getKey()).get(entry.getKey()) == 1 &&
                        diffMatrix.get(entryData.getKey()).get(entry.getKey()) == 0) {
                    filmSet.add(entry.getKey());
                }
            }
        }
        return filmSet;
    }
}
