package ru.igrey.dev.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.igrey.dev.dao.mapper.NoteMapper;
import ru.igrey.dev.entity.NoteEntity;

import java.util.List;

/**
 * Created by sanasov on 26.04.2017.
 */
public class NoteDao {
    private JdbcTemplate jdbcTemplate;

    public NoteDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public NoteEntity save(NoteEntity entity) {
        if (findById(entity.getId()) == null) {
            return insert(entity);
        } else {
            return update(entity);
        }
    }

    public NoteEntity insert(NoteEntity entity) {
        String sqlInsert = "INSERT INTO note (category_id, user_id, txt, notify_rule, file_name, caption, timezone_minutes)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlInsert, new Object[]{
                entity.getCategoryId(),
                entity.getUserId(),
                entity.getText(),
                entity.getNotifyRule(),
                entity.getFileName(),
                entity.getCaption(),
                entity.getTimezoneInMinutes()
        });
        entity.setId(jdbcTemplate.queryForLong("SELECT seq from sqlite_sequence WHERE name = 'note'"));
        return entity;
    }

    public NoteEntity update(NoteEntity entity) {
        String sqlUpdate = "update note set" +
                " category_id = ?," +
                " user_id = ?," +
                " txt = ?," +
                " notify_rule = ?," +
                " file_name = ?," +
                " caption = ?," +
                " timezone_minutes = ?" +
                " where id = ?";
        jdbcTemplate.update(sqlUpdate, new Object[]{
                entity.getCategoryId(),
                entity.getUserId(),
                entity.getText(),
                entity.getNotifyRule(),
                entity.getFileName(),
                entity.getCaption(),
                entity.getTimezoneInMinutes(),
                entity.getId()
        });
        return entity;
    }

    public void delete(Long entityId) {
        String sqlDelete = "delete from note where id = ?";
        jdbcTemplate.update(sqlDelete, new Object[]{entityId});
    }

    public void deleteByCategoryId(Long categoryId) {
        String sqlDelete = "delete from note where category_id = ?";
        jdbcTemplate.update(sqlDelete, new Object[]{categoryId});
    }

    public NoteEntity findById(Long id) {
        String sql = "SELECT * FROM note WHERE ID = ?";
        List<NoteEntity> entityEntities = jdbcTemplate.query(
                sql, new Object[]{id}, new NoteMapper());
        return entityEntities.isEmpty() ? null : entityEntities.get(0);
    }

    public List<NoteEntity> findByUserId(Long userId) {
        String sql = "SELECT * FROM note WHERE user_id = ?";
        List<NoteEntity> entities = jdbcTemplate.query(
                sql, new Object[]{userId}, new NoteMapper());
        return entities;
    }

    public List<NoteEntity> findByCategoryId(Long categoryId) {
        String sql = "SELECT * FROM note WHERE category_id = ?";
        List<NoteEntity> entities = jdbcTemplate.query(
                sql, new Object[]{categoryId}, new NoteMapper());
        return entities;
    }

    public List<NoteEntity> findAllNewUserNotes(Long userId) {
        String sql = "SELECT * FROM note WHERE category_id is null and user_id = ?";
        List<NoteEntity> entities = jdbcTemplate.query(
                sql, new Object[]{userId}, new NoteMapper());
        return entities;
    }

    public NoteEntity findLastInsertedNoteWithoutCategory(Long userId) {
        String sql = "SELECT * FROM note WHERE ID = (SELECT MAX(ID) FROM note WHERE USER_ID = ? AND category_id is null)";
        List<NoteEntity> entities = jdbcTemplate.query(
                sql, new Object[]{userId}, new NoteMapper());
        return entities.isEmpty() ? null : entities.get(0);
    }
}
