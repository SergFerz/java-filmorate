package ru.yandex.practicum.filmorate.model;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Recommender {

    private final JdbcTemplate jdbcTemplate;

    public Recommender(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Set<Long> getRecommendations(long userId) {
        Map<Long, Map<Long, Double>> diffMatrix = new HashMap<>();
        Map<Long, Integer> freq = new HashMap<>();
        Map<Long, Map<Long, Double>> data = buildDifferencesMatrix();
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

    private Map<Long, Map<Long, Double>> buildDifferencesMatrix() {
        Map<Long, Map<Long, Double>> data = new HashMap<>();
        HashMap<Long, Double> films = new HashMap<>();
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet("SELECT * FROM likes");
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT film_id FROM likes group by film_id");
        while (filmRows.next()) {
            films.put(filmRows.getLong("film_id"), 0.0);
        }
        while (likeRows.next()) {
            if (!data.containsKey(likeRows.getLong("user_id"))) {
                data.put(likeRows.getLong("user_id"), new HashMap<>(films));
            }
            data.get(likeRows.getLong("user_id")).put(likeRows.getLong("film_id"), 1.0);
        }
        return data;
    }
}
