package ru.igrey.dev.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.igrey.dev.entity.NoteEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by sanasov on 26.04.2017.
 */
public class NoteMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new NoteEntity(
                rs.getLong("ID"),
                LocalDateTime.parse(rs.getString("CREATE_DATE"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                rs.getString("TXT"),
                rs.getLong("CATEGORY_ID"),
                rs.getLong("USER_ID"),
                rs.getString("NOTIFY_RULE"),
                rs.getString("FILE_NAME"),
                rs.getString("CAPTION"),
                rs.getInt("TIMEZONE_MINUTES")
        );
    }
}