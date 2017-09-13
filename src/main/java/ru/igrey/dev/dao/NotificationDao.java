package ru.igrey.dev.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.igrey.dev.dao.mapper.NotificationMapper;
import ru.igrey.dev.entity.NotificationEntity;

import java.util.List;

/**
 * Created by sanasov on 26.04.2017.
 */
public class NotificationDao {
    private JdbcTemplate jdbcTemplate;

    public NotificationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public NotificationEntity insert(NotificationEntity entity) {
        String sqlInsert = "INSERT INTO notification (notify_date,  note_id, user_id, note_txt)"
                + " VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sqlInsert, new Object[]{
                entity.getNotifyDate(),
                entity.getNoteId(),
                entity.getUserId(),
                entity.getNote()
        });
        return entity;
    }

    public void deleteByNoteId(Long noteId) {
        String sqlDelete = "delete from notification where note_id = ?";
        jdbcTemplate.update(sqlDelete, new Object[]{noteId});
    }
    public void deleteById(Long id) {
        String sqlDelete = "delete from notification where id = ?";
        jdbcTemplate.update(sqlDelete, new Object[]{id});
    }

    public List<NotificationEntity> findAll() {
        String sql = "SELECT * FROM notification";
        List<NotificationEntity> entityEntities = jdbcTemplate.query(
                sql, new Object[]{}, new NotificationMapper());
        return entityEntities.isEmpty() ? null : entityEntities;
    }

}
