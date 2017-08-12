package ru.igrey.dev.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.igrey.dev.dao.mapper.NoteMapper;
import ru.igrey.dev.entity.CategoryEntity;

import java.util.List;

/**
 * Created by sanasov on 26.04.2017.
 */
public class CategoryDao {
    private JdbcTemplate jdbcTemplate;

    public CategoryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(CategoryEntity entity) {
        if (findById(entity.getId()) == null) {
            insert(entity);
        } else {
            update(entity);
        }
    }

    public void insert(CategoryEntity entity) {
        String sqlInsert = "INSERT INTO category (user_id, title)"
                + " VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlInsert, new Object[]{
                entity.getUserId(),
                entity.getTitle()
        });
    }

    public void update(CategoryEntity entity) {
        String sqlUpdate = "update category set" +
                " user_id = ?," +
                " title = ?" +
                " where id = ?";
        jdbcTemplate.update(sqlUpdate, new Object[]{
                entity.getUserId(),
                entity.getTitle(),
                entity.getId()
        });
    }

    public void delete(Long entityId) {
        String sqlDelete = "delete from category where id = ?";
        jdbcTemplate.update(sqlDelete, new Object[]{entityId});
    }

    public CategoryEntity findById(Long id) {
        String sql = "SELECT * FROM category WHERE ID = ?";
        List<CategoryEntity> entities = jdbcTemplate.query(
                sql, new Object[]{id}, new NoteMapper());
        return entities.isEmpty() ? null : entities.get(0);
    }

    public List<CategoryEntity> findByUserId(Long userId) {
        String sql = "SELECT * FROM category WHERE user_id = ?";
        List<CategoryEntity> entities = jdbcTemplate.query(
                sql, new Object[]{userId}, new NoteMapper());
        return entities;
    }

    public CategoryEntity findByUserIdAndTitle(Long userId, String title) {
        String sql = "SELECT * FROM category WHERE user_id = ? and title = ?";
        List<CategoryEntity> entities = jdbcTemplate.query(
                sql, new Object[]{userId, title}, new NoteMapper());
        return entities.isEmpty() ? null : entities.get(0);
    }
}
