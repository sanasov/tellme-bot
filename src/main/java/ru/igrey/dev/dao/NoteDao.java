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

    public void save(NoteEntity entity) {
        if (findById(entity.getId()) == null) {
            insert(entity);
        } else {
            update(entity);
        }
    }

    public void insert(NoteEntity entity) {
        String sqlInsert = "INSERT INTO note (category_id, user_id, txt)"
                + " VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlInsert, new Object[]{
                entity.getCategoryId(),
                entity.getUserId(),
                entity.getText()
        });
    }

    public void update(NoteEntity entity) {
        String sqlUpdate = "update note set" +
                " category_id = ?," +
                " user_id = ?," +
                " txt = ?" +
                " where id = ?";
        jdbcTemplate.update(sqlUpdate, new Object[]{
                entity.getCategoryId(),
                entity.getUserId(),
                entity.getText(),
                entity.getId()
        });
    }

    public void delete(Long entityId) {
        String sqlDelete = "delete from note where id = ?";
        jdbcTemplate.update(sqlDelete, new Object[]{entityId});
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
}
