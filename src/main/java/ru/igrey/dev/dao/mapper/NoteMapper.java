package ru.igrey.dev.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.igrey.dev.entity.NoteEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sanasov on 26.04.2017.
 */
public class NoteMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new NoteEntity(
                rs.getLong("ID"),
                rs.getTimestamp("CREATE_DATE").toLocalDateTime(),
                rs.getString("TXT"),
                rs.getLong("CATEGORY_ID"),
                rs.getLong("USER_ID")

        );
    }
}