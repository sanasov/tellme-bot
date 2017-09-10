package ru.igrey.dev.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.igrey.dev.entity.NotificationEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by sanasov on 26.04.2017.
 */
public class NotificationMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new NotificationEntity(
                rs.getString("NOTE_TXT"),
                LocalDateTime.parse(rs.getString("NOTIFY_DATE").replace("T", " "), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                rs.getLong("USER_ID"),
                rs.getLong("NOTE_ID")
        );
    }
}