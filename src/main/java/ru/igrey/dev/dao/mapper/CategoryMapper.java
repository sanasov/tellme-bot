package ru.igrey.dev.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.igrey.dev.entity.CategoryEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sanasov on 26.04.2017.
 */
public class CategoryMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CategoryEntity(
                rs.getLong("ID"),
                rs.getLong("USER_ID"),
                rs.getTimestamp("CREATE_DATE").toLocalDateTime(),
                rs.getString("TITLE")
        );
    }
}