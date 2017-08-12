package ru.igrey.dev.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.igrey.dev.dao.mapper.CategoryMapper;
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

    public CategoryEntity save(CategoryEntity entity) {
        if (findById(entity.getId()) == null) {
            return insert(entity);
        } else {
            return update(entity);
        }
    }

    public CategoryEntity insert(CategoryEntity entity) {
        String sqlInsert = "INSERT INTO category (user_id, title)"
                + " VALUES (?, ?)";
        jdbcTemplate.update(sqlInsert, new Object[]{
                entity.getUserId(),
                entity.getTitle()
        });
        entity.setId(jdbcTemplate.queryForLong("SELECT seq from sqlite_sequence WHERE name = 'category'"));
        return entity;
    }

    public CategoryEntity update(CategoryEntity entity) {
        String sqlUpdate = "update category set" +
                " user_id = ?," +
                " title = ?" +
                " where id = ?";
        jdbcTemplate.update(sqlUpdate, new Object[]{
                entity.getUserId(),
                entity.getTitle(),
                entity.getId()
        });
        return entity;
    }

    public void delete(Long entityId) {
        String sqlDelete = "delete from category where id = ?";
        jdbcTemplate.update(sqlDelete, new Object[]{entityId});
    }

    public CategoryEntity findById(Long id) {
        String sql = "SELECT * FROM category WHERE ID = ?";
        List<CategoryEntity> entities = jdbcTemplate.query(
                sql, new Object[]{id}, new CategoryMapper());
        return entities.isEmpty() ? null : entities.get(0);
    }

    public List<CategoryEntity> findByUserId(Long userId) {
        String sql = "SELECT * FROM category WHERE user_id = ?";
        List<CategoryEntity> entities = jdbcTemplate.query(
                sql, new Object[]{userId}, new CategoryMapper());
        return entities;
    }

    public CategoryEntity findByUserIdAndTitle(Long userId, String title) {
        String sql = "SELECT * FROM category WHERE user_id = ? and title = ?";
        List<CategoryEntity> entities = jdbcTemplate.query(
                sql, new Object[]{userId, title}, new CategoryMapper());
        return entities.isEmpty() ? null : entities.get(0);
    }
}
