package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.Delete;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class DirectorDaoImpl implements DirectorDao {

    private final JdbcTemplate jdbcTemplate;

    public DirectorDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Director> getAllDirectors() {
        String sql = "SELECT * FROM directors";
        List<Director> directorList = jdbcTemplate.query(sql, (rs, rowNum) -> makeDirector(rs));
        log.debug("Текущее количество directors: {}", directorList.size());
        return directorList;
    }

    @Override
    public Director createDirector(Director director) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("directors").usingGeneratedKeyColumns("id");
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", director.getName());
        Number num = jdbcInsert.executeAndReturnKey(parameters);
        if (getDirectorById(num.longValue()).isPresent()) {
            return getDirectorById(num.longValue()).get();
        } else return null;
    }

    @Override
    public Director updateDirector(Director director) {
        jdbcTemplate.update("UPDATE directors SET name=? WHERE id=?", director.getName(), director.getId());
        return getDirectorById(director.getId()).get();
    }

    @Override
    public Optional<Director> getDirectorById(long id) {
        SqlRowSet directorRows = jdbcTemplate.queryForRowSet("SELECT * FROM directors WHERE id = ?", id);
        if (directorRows.next()) {
            Director director = new Director(
                    directorRows.getInt("id"),
                    directorRows.getString("name")
            );
            log.info("Найден director: {} {}", director.getId(), director.getName());
            return Optional.of(director);
        } else {
            log.info("director с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteDirectorById(long id) {
        return jdbcTemplate.update("DELETE FROM directors WHERE id=?", id) > 0;
    }

    private Director makeDirector(ResultSet rs) throws SQLException {
        return new Director(
                rs.getInt("id"),
                rs.getString("name")
        );
    }
}
