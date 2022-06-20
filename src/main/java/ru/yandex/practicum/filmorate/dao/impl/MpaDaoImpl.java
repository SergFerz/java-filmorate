package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "select * from mpa";
        List<Mpa> mpaList = jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
        log.debug("Текущее количество mpa: {}", mpaList.size());
        return mpaList;
    }

    @Override
    public Optional<Mpa> getMpaById(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from mpa where id = ?", id);
        if (userRows.next()) {
            Mpa mpa = new Mpa(
                    userRows.getInt("id"),
                    userRows.getString("name")
            );
            log.info("Найден mpa: {} {}", mpa.getId(), mpa.getName());
            return Optional.of(mpa);
        } else {
            log.info("mpa с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        return new Mpa(
                rs.getInt("id"),
                rs.getString("name")
        );
    }
}
