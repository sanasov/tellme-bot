package ru.igrey.dev.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.igrey.dev.entity.CategoryEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by sanasov on 26.04.2017.
 */
public class CategoryMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CategoryEntity(
                rs.getLong("ID"),
                rs.getLong("USER_ID"),
                LocalDateTime.parse(rs.getString("CREATE_DATE"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                rs.getString("TITLE")
        );
    }
}